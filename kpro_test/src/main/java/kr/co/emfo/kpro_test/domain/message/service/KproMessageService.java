package kr.co.emfo.kpro_test.domain.message.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.emfo.kpro_test.domain.api.client.KproApiClient;
import kr.co.emfo.kpro_test.domain.api.dto.KproApiRequest;
import kr.co.emfo.kpro_test.domain.message.converter.MessageConverter;
import kr.co.emfo.kpro_test.domain.message.entity.Message;
import kr.co.emfo.kpro_test.domain.message.entity.MessageLog;
import kr.co.emfo.kpro_test.domain.message.repository.MessageLogRepository;
import kr.co.emfo.kpro_test.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KproMessageService {

    private final MessageRepository messageRepository;
    private final MessageLogRepository messageLogRepository;

    @Autowired
    private final KproApiClient kproApiClient;
    @Autowired
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void pendingMessages() {
        List<Message> messageList = messageRepository.findAllByCurState('F')
                .orElse(new ArrayList<>());
        for (Message message : messageList) {
            try {
                message.updateCurState('0');
                messageRepository.saveAndFlush(message);

                // message -> requestDto 변환
                KproApiRequest.SendKproMessageRequestDto request = MessageConverter.toSendMessageDto(message);

                List<KproApiRequest.SendKproMessageRequestDto> requestList = new ArrayList<>();
                requestList.add(request);

                String response = kproApiClient.sendKproMessage(requestList);

                message.updateCurState('2');
                messageRepository.saveAndFlush(message);

                JsonNode rootNode = objectMapper.readTree(response);
                JsonNode results = rootNode.get("messages");
                if (results != null && results.isArray()) {
                    for (JsonNode resultNode : results) {

                        Long idx = resultNode.get("idx").asLong();
                        String log = "";
                        int i = 1;

                        loop:
                        while(i < 2) {

                            String tempLog = getLog(idx);

                            JsonNode tempRootNode = objectMapper.readTree(tempLog);
                            JsonNode tempLogNodes = tempRootNode.get("logs");

                            if (tempLogNodes != null && tempLogNodes.isArray()) {
                                for (JsonNode tempLogNode : tempLogNodes) {
                                    String tempCurState = tempLogNode.get("cur_state").asText();
                                    System.out.println("cs : " + tempCurState);

                                    if (tempCurState.equals("4")) {
                                        log = tempLog;
                                        System.out.println(log);
                                        break loop;
                                    }
                                }
                            }

                            Thread.sleep(5000);
                        }

                        MessageLog saveLog = saveLog(log);

                        messageLogRepository.save(saveLog);
                    }
                }


            } catch (Exception e) {
                message.updateCurState('6');
            } finally {
                messageRepository.saveAndFlush(message);
                List<Message> deleteMessageList = messageRepository.findAllByCurState('2')
                        .orElse(new ArrayList<>());

                messageRepository.deleteAll(deleteMessageList);
            }
        }
    }

    public MessageLog saveLog(String response) throws JsonProcessingException {

        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode logs = rootNode.get("logs");

        MessageLog messageLog = new MessageLog();

        if (logs != null && logs.isArray()) messageLog = MessageConverter.toMessageLog(logs.get(0));


        return messageLog;
    }

    public String getLog(Long i) {
        String logs = kproApiClient.getKproLog(i);
        System.out.println(logs);

        return logs;
    }

    public void getLogs() {
        String logs = kproApiClient.getKproLogs();
        System.out.println(logs);
    }
}
