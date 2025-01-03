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
import kr.co.emfo.kpro_test.global.response.exception.handler.NproHandler;
import kr.co.emfo.kpro_test.global.response.exception.handler.ServerHandler;
import kr.co.emfo.kpro_test.global.util.PwdUtil;
import kr.co.emfo.kpro_test.global.util.SerialNumberUtil;
import kr.co.emfo.kpro_test.global.validator.ValidProcess;
import kr.co.emfo.kpro_test.manager.MapManager;
import kr.co.emfo.kpro_test.manager.NproResultResponseManager;
import kr.co.emfo.kpro_test.manager.QueueManager;
import kr.co.emfo.kpro_test.manager.ThreadPoolManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final MapManager mapManager;
    private final ThreadPoolManager threadPoolManager;

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
    public void nproLogCheckAndSave() throws IllegalAccessException {
        if (!queueManager.isQueueEmpty()) {
            long startTime = System.currentTimeMillis();

            ExecutorService logPool;
            int threadPoolNum = 3;

            String sendSuccess = "2";

            while(!queueManager.isQueueEmpty()) {
                logPool = threadPoolManager.getLogPool(threadPoolNum);
                CountDownLatch latch = new CountDownLatch(threadPoolNum);

                for (int i = 0; i < threadPoolNum; i++) {
                    logPool.submit(() -> {
                        try {
                            if (!queueManager.isQueueEmpty()) {
                                Long currentIdx = queueManager.getIdxFromQueue();
                                boolean sendingMessageComplete = false;

                                System.out.println("[" + Thread.currentThread().getName() + "] IDX - " + currentIdx + " is logging..");

                                while (!sendingMessageComplete) {
                                    sendingMessageComplete = checkStateFromLog(currentIdx);

                                    if (!sendingMessageComplete) {
                                        Thread.sleep(5000);
                                    }
                                    else {
                                        saveNproMessageLog(currentIdx);
                                    }
                                }
                            }
                            throw new InterruptedException();
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
            System.out.println("🔄 [Total Time] Processing completed in " + (endTime - startTime) + " ms");

            responseAboutCode();
        }
    }

    private void responseAboutCode() throws IllegalAccessException {
        while (!mapManager.isMapEmpty()) {
            Map.Entry<Long, String> currentMap = mapManager.getMapEntryData();

            NproResultResponseManager.printMessageAboutCode(currentMap.getKey(), currentMap.getValue());
        }
    }

    private boolean checkStateFromLog(Long idx) {
        try {
            String log = getLogs(idx, id);

            JsonNode logRootNode = objectMapper.readTree(log);
            JsonNode logNode = logRootNode.get("log");

            if (logNode != null && !logNode.isArray()) {

                System.out.println("테스트 발송입니다.");
                System.out.println("[NproMessageLog]: " + log);

                if (logNode.asText().equals("NO_DATA")) {
                    System.err.println("Npro2 Bad Request: " + ErrorStatus.NPRO_BAD_REQUEST.getMessage());
                    throw new NproHandler(ErrorStatus.NPRO_BAD_REQUEST);
                }

                mapManager.addToMap(idx, logNode.asText());

                return true;
            }
            else if (logNode != null && logNode.isArray()) {
                for (JsonNode currentLog : logNode) {
                    String tempCode = currentLog.get("CODE").asText();

                    if (!tempCode.equals(" ")) {
                        System.out.println("IDX['" + idx + "'] Processing Complete");

                        mapManager.addToMap(idx, currentLog.get("CODE").asText());

                        return true;
                    }
                }
            }

            return false;
        } catch (JsonProcessingException e) {
            throw new ServerHandler(ErrorStatus.JSON_PARSING_EXCEPTION);
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
    private void saveNproMessageLog(Long idx) {
        try {
            ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();

            NproMessageLog nproMessageLog = new NproMessageLog();

            String log = getLogs(idx, id);
            JsonNode logRootNode = objectMapper.readTree(log);
            JsonNode logNode = logRootNode.get("log");

            for (JsonNode currentLog : logNode) {
                System.out.println("전송 및 로그 작성 완료");
                System.out.println("[NproMessageLog]: " + writer.writeValueAsString(logNode));
                nproMessageLog = MessageConverter.toNproMessageLog(idx, currentLog);
            }

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
