package kr.co.emfo.kpro_test.domain.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import feign.FeignException;
import jakarta.annotation.PreDestroy;
import kr.co.emfo.kpro_test.domain.api.client.KproApiClient;
import kr.co.emfo.kpro_test.domain.api.dto.KproApiRequest;
import kr.co.emfo.kpro_test.domain.message.converter.MessageConverter;
import kr.co.emfo.kpro_test.domain.message.entity.Message;
import kr.co.emfo.kpro_test.domain.message.entity.MessageLog;
import kr.co.emfo.kpro_test.domain.message.repository.MessageLogRepository;
import kr.co.emfo.kpro_test.domain.message.repository.MessageRepository;
import kr.co.emfo.kpro_test.global.response.code.resultCode.ErrorStatus;
import kr.co.emfo.kpro_test.global.response.exception.handler.DataHandler;
import kr.co.emfo.kpro_test.global.response.exception.handler.ServerHandler;
import kr.co.emfo.kpro_test.global.validator.ValidProcess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KproMessageService {

    private final MessageRepository messageRepository;
    private final MessageLogRepository messageLogRepository;
    private final ValidProcess validProcess;
    private final QueueManager queueManager;

    private final KproApiClient kproApiClient;
    private final ObjectMapper objectMapper;

    private ExecutorService logPool;

    @Scheduled(fixedDelay = 10000)
    public void sendingKproMessages() {

        try {

            Character inDb = 'F'; // DB에 Insert 한 상태
            Character readData = '0'; // DB에서 Select 한 상태
            Character sending = '2'; // 메시지 전송 요청을 한 상태
            Character sendFail = '6'; // 메시지 전송이 실패한 상태

            List<KproApiRequest.SendKproMessageRequestDto> requestDtoList = new ArrayList<>();

            List<Message> messageList = messageRepository.findTop1000ByCurState(inDb)
                    .orElse(new ArrayList<>());

            System.out.println("sendingKproMessages() is running... 총 개수 : " + messageList.size());

            // 요청 한 번에 1000개를 넘는 메시지 전송 시 예외 처리 -> 테스트 안함
            if (messageList.size() > 1000) {

                updateCurStateAndSaveForAll(sendFail, messageList);
                throw new DataHandler(ErrorStatus.ONE_REQUEST_MAX_1000);
            }
            else if (!messageList.isEmpty()) {

                StringBuilder sb = new StringBuilder();
                sb.append("=========[Kpro 유효성 검사 시작]========\n");
                sb.append("유효성 검사 통과 실패 메시지 ID List: [ ");
                for (Message message : messageList) {

                    try {

                        KproApiRequest.SendKproMessageRequestDto requestDto = MessageConverter.toSendKproMessageDto(message);
                        validProcess.validateKproRequestDto(requestDto); // 유효성 검사
                        requestDtoList.add(requestDto);

                        updateCurStateAndSave(readData, message);
                    } catch (Exception e) {

                        updateCurStateAndSave(sendFail, message);
                        sb.append(message.getId() + " ");
                        System.err.println("message.id['" + message.getId() + "'] request Fail.");
                        System.err.println("Validation Error: " + e.getMessage());
                    }
                }
                sb.append("] \n");
                sb.append("=========[Kpro 유효성 검사 종료]========\n");

                System.out.println(sb.toString());

                String response = kproApiClient.sendKproMessage(requestDtoList);

                JsonNode requestSendingMessageResponseRootNode = objectMapper.readTree(response);
                JsonNode resultListNode = requestSendingMessageResponseRootNode.get("messages");

                ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

                System.out.println("전송 실패 건 제외 나머지 건 전송 요청 성공.");
                System.out.println("[Response] : " + writer.writeValueAsString(requestSendingMessageResponseRootNode));

                if (resultListNode != null && resultListNode.isArray()) {

                    for (JsonNode result : resultListNode) {

                        Long idx = result.get("idx").asLong();
                        queueManager.addToQueue(idx);
                    }
                }

                for (Message message : getMessageList(messageList)) {

                    if (!message.getCurState().equals('6')) {

                        updateCurStateAndSave(sending, message);
                    }
                }
            }

        } catch (JsonProcessingException e) {

            throw new ServerHandler(ErrorStatus.JSON_PARSING_EXCEPTION);
        } catch (FeignException e) {

            System.err.println("Feign Error: " + e);
        }

    }

    private static List<Message> getMessageList(List<Message> messageList) {
        return messageList;
    }

    @Scheduled(fixedDelay = 10000)
    public void kproLogCheckAndSave() {

        if (!queueManager.isQueueEmpty()) {

            long startTime = System.currentTimeMillis();
            Character sending = '2'; // 메시지 전송 요청을 한 상태
            Character sendSuccess = '4'; // 성공적으로 메시지 전송이 완료된 상태

            while (!queueManager.isQueueEmpty()) {

                if (logPool == null || logPool.isShutdown() || logPool.isTerminated()) {

                    logPool = Executors.newFixedThreadPool(3);
                }

                CountDownLatch latch = new CountDownLatch(3);

                for (int i = 0; i < 3; i++) {

                    logPool.submit(() -> {

                        try {

                            System.out.println("[Work] Kpro Thread Name: " + Thread.currentThread().getName());

                            if (!queueManager.isQueueEmpty()) {

                                Long currentIdx = queueManager.getIdxFromQueue();
                                boolean sendingMessageComplete = false;

                                System.out.println("[" + Thread.currentThread().getName() + "] IDX - " + currentIdx + " is logging..");

                                while(!sendingMessageComplete) {

                                    String log = getLog(currentIdx);

                                    JsonNode logRootNode = objectMapper.readTree(log);
                                    JsonNode logNode = logRootNode.get("logs");

                                    if (logNode != null && logNode.isArray()) {

                                        for (JsonNode currentLog : logNode) {

                                            String tempCurState = currentLog.get("cur_state").asText();

                                            if (tempCurState.equals(sendSuccess.toString())) {

                                                System.out.println("IDX['" + currentIdx + "'] Processing Complete");
                                                sendingMessageComplete = true;

                                                saveMessageLog(currentLog);
                                            }
                                        }
                                    }

                                    if (!sendingMessageComplete) {

                                        Thread.sleep(5000);
                                    }
                                }
                            }
                        } catch (JsonProcessingException e) {

                            throw new ServerHandler(ErrorStatus.JSON_PARSING_EXCEPTION);
                        } catch (InterruptedException e) {

                            Thread.currentThread().interrupt();
                            throw new ServerHandler(ErrorStatus.THREAD_INTERRUPTED);
                        } finally {

                            System.out.println("[Finish] Kpro Thread Name : " + Thread.currentThread().getName());
                            latch.countDown();
                        }
                    });
                }

                try {

                    latch.await();
                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                    System.err.println("[Error] Latch interrupted while waiting.");
                }
            }

            // 로깅 완료 후 전송 성공한 Message Data Delete
            deleteSentMessage(sending);

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("🔄 [Total Time] Processing completed in " + totalTime + " ms");

        }
    }

    @Transactional
    private void updateCurStateAndSave(Character status, Message message) {

        message.updateCurState(status);
        messageRepository.saveAndFlush(message);
    }

    @Transactional
    private void updateCurStateAndSaveForAll(Character status, List<Message> messageList) {

        for (Message message : messageList) {

            message.updateCurState(status);
            messageRepository.save(message);
        }

        messageRepository.flush();
    }

    @Transactional
    public void saveMessageLog(JsonNode logs) {

        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

            System.out.println("전송 및 로그 작성 완료.");
            System.out.println("[KproMessageLog]: " + writer.writeValueAsString(logs));
            MessageLog messageLog = MessageConverter.toMessageLog(logs);

            messageLogRepository.save(messageLog);
        } catch (JsonProcessingException e) {

            throw new ServerHandler(ErrorStatus.JSON_PARSING_EXCEPTION);
        }
    }

    public String getLog(Long i) {

        return kproApiClient.getKproLog(i);
    }

    public void getLogs() {
        String logs = kproApiClient.getKproLogs();
        System.out.println(logs);
    }

    private void deleteSentMessage(Character currentState) {

        List<Message> messageList = messageRepository.findAllByCurState(currentState)
                .orElse(new ArrayList<>());

        messageRepository.deleteAll(messageList);
    }
}
