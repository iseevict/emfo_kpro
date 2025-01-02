package kr.co.emfo.kpro_test.domain.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import kr.co.emfo.kpro_test.domain.api.client.NproApiClient;
import kr.co.emfo.kpro_test.domain.api.dto.NproApiRequest;
import kr.co.emfo.kpro_test.domain.message.converter.MessageConverter;
import kr.co.emfo.kpro_test.domain.message.entity.NproMessage;
import kr.co.emfo.kpro_test.domain.message.entity.NproMessageLog;
import kr.co.emfo.kpro_test.domain.message.repository.NproMessageLogRepository;
import kr.co.emfo.kpro_test.domain.message.repository.NproMessageRepository;
import kr.co.emfo.kpro_test.global.response.code.resultCode.ErrorStatus;
import kr.co.emfo.kpro_test.global.response.exception.handler.ServerHandler;
import kr.co.emfo.kpro_test.global.util.PwdUtil;
import kr.co.emfo.kpro_test.global.util.SerialNumberUtil;
import kr.co.emfo.kpro_test.global.validator.ValidProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class NproMessageService {

    private final NproMessageRepository nproMessageRepository;
    private final NproMessageLogRepository nproMessageLogRepository;

    private final NproApiClient nproApiClient;
    private final ObjectMapper objectMapper;

    private final ValidProcess validProcess;
    private final QueueManager queueManager;

    private ExecutorService logPool;

    @Value("${server.id}")
    private String id;

    @Scheduled(fixedDelay = 10000)
    public void sendNpro2Messages() {

        String inDb = "F";
        String readData = "0";
        String sending = "2";
        String sendFail = "6";

        List<NproMessage> nproMessageList = nproMessageRepository.findTop1000ByState(inDb)
                .orElse(new ArrayList<>());

        System.out.println("sendingNpro2Messages() is running... 총 개수 : " + nproMessageList.size());

        for (NproMessage nproMessage : nproMessageList) {

            // random SerialNum
            Long idx = Long.valueOf(SerialNumberUtil.generateSerialNumber());

            try {

                updateStateAndSave(readData, nproMessage);

                // select Data -> requestDTO
                NproApiRequest.SendNproMessageRequestDto request = MessageConverter.toSendNproMessageRequestDto(nproMessage);
                validProcess.validateNproRequestDto(request);

                String response = nproApiClient.sendNproMessage(
                        idx,
                        request.getMId(),
                        PwdUtil.pwdMd5(request.getMPwd()),
                        request.getCallTo(),
                        request.getCallFrom(),
                        request.getMSubject(),
                        request.getMMessage(),
                        request.getMType(),
                        request.getMSendType(),
                        request.getMFileName(),
                        request.getMYyyy(),
                        request.getMMm(),
                        request.getMDd(),
                        request.getMHh(),
                        request.getMMi(),
                        request.getUrlSuccess(),
                        request.getUrlFail(),
                        request.getFlagTest(),
                        request.getFlagDeny(),
                        request.getFlagMerge()
                );

                updateStateAndSave(sending, nproMessage);

                queueManager.addToQueue(idx);
            } catch (Exception e) {

                updateStateAndSave(sendFail, nproMessage);
                System.err.println("유효성 검사 통과 실패. [idx - " + idx + "]");
                System.err.println("Validation Error: " + e.getMessage());
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void nproLogCheckAndSave() {

        if (!queueManager.isQueueEmpty()) {

            long startTime = System.currentTimeMillis();
            String sendSuccess = "2";

            while(!queueManager.isQueueEmpty()) {

                if (logPool == null || logPool.isShutdown() || logPool.isTerminated()) {

                    logPool = Executors.newFixedThreadPool(3);
                }

                CountDownLatch latch = new CountDownLatch(3);

                for (int i = 0; i < 3; i++) {

                    logPool.submit(() -> {

                        try {

                            System.out.println("[Work] Npro Thread Name: " + Thread.currentThread().getName());

                            if (!queueManager.isQueueEmpty()) {

                                Long currentIdx = queueManager.getIdxFromQueue();
                                boolean sendingMessageComplete = false;

                                System.out.println("[" + Thread.currentThread().getName() + "] IDX - " + currentIdx + " is logging..");

                                while (!sendingMessageComplete) {

                                    String log = getLogs(currentIdx, id);

                                    JsonNode logRootNode = objectMapper.readTree(log);
                                    JsonNode logNode = logRootNode.get("log");

                                    if (logNode != null && !logNode.isArray()) {

                                        System.out.println("테스트 발송입니다.");
                                        System.out.println("[NproMessageLog]: " + log);
                                        sendingMessageComplete = true;
                                    }
                                    else if (logNode != null && logNode.isArray()) {

                                        for (JsonNode currentLog : logNode) {

                                            String tempCode = currentLog.get("CODE").asText();

                                            if (!tempCode.equals(" ")) {

                                                System.out.println("IDX['" + currentIdx + "'] Processing Complete");
                                                sendingMessageComplete = true;

                                                saveNproMessageLog(currentIdx, currentLog);
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

                            System.out.println("[Finish] Npro Thread Name : " + Thread.currentThread().getName());
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

            deleteSentMessage(sendSuccess);

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            System.out.println("🔄 [Total Time] Processing completed in " + totalTime + " ms");

        }
    }

    public String getLogs(Long mIdx, String mId) {

        String logs = nproApiClient.getNproLog(mIdx, mId);
        System.out.println(logs);

        return logs;
    }

    @Transactional
    private void updateStateAndSave(String state, NproMessage nproMessage) {

        nproMessage.updateState(state);
        nproMessageRepository.saveAndFlush(nproMessage);
    }

    @Transactional
    private void saveNproMessageLog(Long idx, JsonNode log) {

        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

            System.out.println("전송 및 로그 작성 완료");
            System.out.println("[NproMessageLog]: " + writer.writeValueAsString(log));
            NproMessageLog nproMessageLog = MessageConverter.toNproMessageLog(idx, log);

            nproMessageLogRepository.save(nproMessageLog);
        } catch (JsonProcessingException e) {

            throw new ServerHandler(ErrorStatus.JSON_PARSING_EXCEPTION);
        }
    }

    @Transactional
    private void deleteSentMessage(String sendSuccess) {

        List<NproMessage> nproMessageList = nproMessageRepository.findAllByState(sendSuccess)
                .orElse(new ArrayList<>());

        nproMessageRepository.deleteAll(nproMessageList);
    }
}
