package kr.co.emfo.kpro_test.global.response.exception.handler;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.exception.GeneralException;

public class DataHandler extends GeneralException {

    public DataHandler(BaseErrorCode errorCode) {

        super(errorCode);
    }
}
