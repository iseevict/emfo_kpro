package kr.co.emfo.kpro_test.global.response.exception.handler;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.exception.GeneralException;

public class ValidateHandler extends GeneralException {

    public ValidateHandler(BaseErrorCode errorCode) {

        super(errorCode);
    }
}
