package kr.co.emfo.kpro_test.global.response.code.resultCode;

import kr.co.emfo.kpro_test.global.response.code.BaseErrorCode;
import kr.co.emfo.kpro_test.global.response.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL401", "서버 오류"),
    THREAD_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL403", "Thread interrupted!!"),

    // Global - Json
    JSON_PARSING_EXCEPTION(HttpStatus.BAD_REQUEST, "GLOBAL402", "Json parsing or processing had a problem"),

    // Validate
    PHONE_NOT_NULL(HttpStatus.BAD_REQUEST, "DATA401", "phone cannot be null"),
    SENDERKEY_NOT_NULL(HttpStatus.BAD_REQUEST, "DATA401", "senderKey cannot be null"),
    TMPLCD_NOT_NULL(HttpStatus.BAD_REQUEST, "DATA401", "tmplCd cannot be null"),
    SENDMSG_NOT_NULL(HttpStatus.BAD_REQUEST, "DATA401", "sendMsg cannot be null"),
    SMSTYPE_NOT_NULL(HttpStatus.BAD_REQUEST, "DATA401", "smsType cannot be null"),

    PHONE_ONLY_NUM(HttpStatus.BAD_REQUEST, "DATA402", "phone must contain only numbers"),

    PHONE_MAX_LEN(HttpStatus.BAD_REQUEST, "DATA403", "phone must be 10 or 11 digits"),
    SENDMSG_MAX_LEN(HttpStatus.BAD_REQUEST, "DATA403", "sendMsg max length is 500"),

    // Data(DB)
    ONE_REQUEST_MAX_1000(HttpStatus.TOO_MANY_REQUESTS, "DATA404", "Max 1000 messages can be sent at one request"),

    NPRO_BAD_REQUEST(HttpStatus.BAD_REQUEST, "DATA405", "비정상 요청입니다."),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
