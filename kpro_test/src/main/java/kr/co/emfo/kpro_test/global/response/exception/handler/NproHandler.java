package kr.co.emfo.kpro_test.global.response.exception.handler;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.exception.GeneralException;

public class NproHandler extends GeneralException {

    public NproHandler(BaseErrorCode errorCode) {

        super(errorCode);
    }
}
