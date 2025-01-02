package kr.co.emfo.kpro_test.global.validator;

import kr.co.emfo.kpro_test.domain.api.dto.KproApiRequest;
import kr.co.emfo.kpro_test.domain.api.dto.NproApiRequest;
import kr.co.emfo.kpro_test.global.response.code.resultCode.ErrorStatus;
import kr.co.emfo.kpro_test.global.response.exception.handler.ValidateHandler;
import org.springframework.stereotype.Component;

@Component
public class ValidProcess {

    public void validateKproRequestDto (KproApiRequest.SendKproMessageRequestDto request) {

        // 전화번호 유효성 검사
        if (request.getPhone() == null) {

            throw new ValidateHandler(ErrorStatus.PHONE_NOT_NULL);
        }
        else if (!request.getPhone().matches("\\d+")) {

            throw new ValidateHandler(ErrorStatus.PHONE_ONLY_NUM);
        }

        // 센더키 유효성 검사
        if (request.getSenderKey() == null) {

            throw new ValidateHandler(ErrorStatus.SENDERKEY_NOT_NULL);
        }

        // 템플릿 코드 유효성 검사
        if (request.getTmplCd() == null) {

            throw new ValidateHandler(ErrorStatus.TMPLCD_NOT_NULL);
        }

        // 메시지 본문 유효성 검사
        if (request.getSendMsg() == null) {

            throw new ValidateHandler(ErrorStatus.SENDMSG_NOT_NULL);
        }
        else if (request.getSendMsg().length() > 500) {

            throw new ValidateHandler(ErrorStatus.SENDMSG_MAX_LEN);
        }

        // 전송 타입 유효성 검사
        if (request.getSmsType() == null) {

            throw new ValidateHandler(ErrorStatus.SMSTYPE_NOT_NULL);
        }
    }

    public void validateNproRequestDto (NproApiRequest.SendNproMessageRequestDto request) {

        // callTo 유효성 검사
        if (request.getCallTo() == null) {

            throw new ValidateHandler(ErrorStatus.PHONE_NOT_NULL);
        }
        else if (!request.getCallTo().matches("\\d+")) {

            throw new ValidateHandler(ErrorStatus.PHONE_ONLY_NUM);
        }

        // callFrom 유효성 검사
        if (request.getCallFrom() == null) {

            throw new ValidateHandler(ErrorStatus.PHONE_NOT_NULL);
        }
        else if (!request.getCallFrom().matches("\\d+")) {

            throw new ValidateHandler(ErrorStatus.PHONE_ONLY_NUM);
        }
    }
}
