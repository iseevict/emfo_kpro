package kr.co.emfo.kpro_test.global.response.exception.handler;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.exception.GeneralException;

public class ServerHandler extends GeneralException {

    public ServerHandler(BaseErrorCode errorCode) {

        super(errorCode);
    }
}
