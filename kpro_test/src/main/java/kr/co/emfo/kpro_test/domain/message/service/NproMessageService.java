package kr.co.emfo.kpro_test.domain.message.service;

import kr.co.emfo.kpro_test.domain.api.client.NproApiClient;
import kr.co.emfo.kpro_test.domain.api.dto.NproApiRequest;
import kr.co.emfo.kpro_test.domain.message.converter.MessageConverter;
import kr.co.emfo.kpro_test.domain.message.entity.NproMessage;
import kr.co.emfo.kpro_test.domain.message.repository.NproMessageRepository;
import kr.co.emfo.kpro_test.global.util.PwdUtil;
import kr.co.emfo.kpro_test.global.util.SerialNumberUtil;
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
public class NproMessageService {

    private final NproMessageRepository nproMessageRepository;

    @Autowired
    private final NproApiClient nproApiClient;

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void sendMessage() {

        List<NproMessage> nproMessageList = nproMessageRepository.findAllByState("F")
                .orElse(new ArrayList<>());

        for (NproMessage nproMessage : nproMessageList) {

            try {

                nproMessage.updateState("0");
                nproMessageRepository.saveAndFlush(nproMessage);
                NproApiRequest.SendNproMessageRequestDto request = MessageConverter.toSendNproMessageRequestDto(nproMessage);

                String response = nproApiClient.sendNproMessage(
                        Long.valueOf(SerialNumberUtil.generateSerialNumber()),
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

                nproMessage.updateState("2");
                nproMessageRepository.saveAndFlush(nproMessage);

                System.out.println("response : "+ response);
            } catch (Exception e) {
                nproMessage.updateState("6");
            } finally {

                nproMessageRepository.saveAndFlush(nproMessage);
            }
        }
    }

    public void getLogs(Long mIdx, String mId) {

        NproApiRequest.NproLogRequestDto request = NproApiRequest.NproLogRequestDto.builder()
                .mIdx(mIdx)
                .mId(mId)
                .build();

        String logs = nproApiClient.getNproLog(request);
        System.out.println(logs);
    }
}
