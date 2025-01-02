package kr.co.emfo.kpro_test.global.response.exception;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException {

    private BaseErrorCode errorCode;

    public GeneralException(BaseErrorCode errorCode) {

        super(errorCode.getReason().getMessage());
        this.errorCode = errorCode;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.errorCode.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}
