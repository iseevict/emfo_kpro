package kr.co.emfo.kpro_test.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KproResultResponseManager {

    SUCCESS_0000("0000", "성공"),
    SUCCESS_1000("1000", "성공"),
    TIMEOUT_2000("2000", "전송 시간 초과"),
    UNEXPECTED_ERROR_2001("2001", "메시지 전송 불가 (예기치 않은 오류 발생)"),
    SERVER_TIMEOUT_3003("3003", "카카오 서버로 메시지 전달 시 TimeOut 발생"),
    DELETED_PROFILE_3005("3005", "삭제된 발신 프로필"),
    MESSAGE_FORMAT_ERROR_3009("3009", "메시지 형식 오류"),
    UNKNOWN_MESSAGE_STATE_3014("3014", "알 수 없는 메시지 상태 (버튼 템플릿 사용 중 버튼 URL 형식 오류)"),
    MSG_TYPE_ERROR_3015("3015", "Msg_type 오류 (1008 또는 1009가 아닌 경우)"),
    JSON_SYNTAX_ERROR_3023("3023", "메시지 문법 오류 (JSON 형식 오류)"),
    INVALID_PROFILE_KEY_3024("3024", "발신 프로필 키가 유효하지 않음"),
    NOT_FRIEND_3025("3025", "메시지 전송 실패 (테스트 시, 친구관계가 아닌 경우)"),
    TEMPLATE_MATCH_ERROR_3026("3026", "메시지와 템플릿의 일치 확인 시 오류 발생"),
    INVALID_PHONE_NUMBER_3027("3027", "카카오톡을 사용하지 않는 사용자 (전화번호 오류 / 050 안심번호)"),
    MESSAGE_NOT_FOUND_3029("3029", "메시지가 존재하지 않음"),
    DUPLICATE_SERIAL_3030("3030", "메시지 일련번호가 중복됨"),
    EMPTY_MESSAGE_3031("3031", "메시지가 비어 있음"),
    MESSAGE_LENGTH_ERROR_3032("3032", "메시지 길이 제한 오류 (공백 포함 1000자)"),
    TEMPLATE_NOT_FOUND_3033("3033", "템플릿을 찾을 수 없음"),
    TEMPLATE_MISMATCH_3034("3034", "메시지가 템플릿과 일치하지 않음"),
    BUTTON_MISMATCH_3036("3036", "버튼 내용이 등록한 템플릿과 일치하지 않음"),
    INVALID_HUB_KEY_3040("3040", "허브 파트너 키가 유효하지 않음"),
    MISSING_NAME_3041("3041", "Request Body에서 Name을 찾을 수 없음"),
    PROFILE_NOT_FOUND_3042("3042", "발신 프로필을 찾을 수 없음"),
    DELETED_PROFILE_3043("3043", "삭제된 발신 프로필"),
    BLOCKED_PROFILE_3044("3044", "차단 상태의 발신 프로필"),
    BLOCKED_PLUS_ID_3045("3045", "차단 상태의 플러스 아이디"),
    CLOSED_PLUS_ID_3046("3046", "닫힘 상태의 플러스 아이디"),
    DELETED_PLUS_ID_3047("3047", "삭제된 플러스 아이디"),
    CONTRACT_INFO_NOT_FOUND_3048("3048", "계약정보를 찾을 수 없음"),
    INTERNAL_SYSTEM_ERROR_3049("3049", "내부 시스템 오류로 메시지 전송 실패"),
    USER_NOT_AVAILABLE_3050("3050", "카카오톡을 사용하지 않는 사용자 또는 사용 불가 상태"),
    MESSAGE_NOT_SENT_3051("3051", "메시지가 발송되지 않은 상태"),
    MESSAGE_INFO_NOT_FOUND_3052("3052", "메시지 확인 정보를 찾을 수 없음"),
    NOT_AVAILABLE_TIME_3054("3054", "메시지 발송 가능한 시간이 아님"),
    GROUP_INFO_NOT_FOUND_3055("3055", "메시지 그룹 정보를 찾을 수 없음"),
    RESULT_NOT_FOUND_3056("3056", "메시지 전송 결과를 찾을 수 없음"),
    POLLING_UNCERTAIN_3060("3060", "사용자에게 발송되었으나 수신 여부 불투명"),
    IMAGE_SEND_ERROR_3061("3061", "이미지 발송 에러 또는 미등록 이미지"),
    INVALID_USER_KEY_3062("3062", "잘못된 형식의 유저키 요청"),
    INVALID_PARAMETER_3063("3063", "잘못된 파라미터 요청"),
    IMAGE_SEND_FAILURE_3064("3064", "이미지를 전송할 수 없음"),
    CONTENT_CORRUPTED_3065("3065", "잘못된 파라미터 요청 (컨텐츠 내용 깨짐)"),
    SYSTEM_ISSUE_9998("9998", "시스템 문제로 서비스 제공 불가"),
    UNKNOWN_SYSTEM_ERROR_9999("9999", "시스템에 알 수 없는 오류 발생"),
    SERVER_BUSY_1001("1001", "서버 과부하 (Queue Full)"),
    INVALID_PHONE_FORMAT_1002("1002", "수신번호 형식 오류"),
    INVALID_REPLY_FORMAT_1003("1003", "회신번호 형식 오류"),
    CLIENT_MSG_KEY_NOT_FOUND_1009("1009", "CLIENT_MSG_KEY 없음"),
    CONTENT_MISSING_1010("1010", "CONTENT 없음"),
    RECIPIENT_INFO_MISSING_1012("1012", "RECIPIENT_INFO 없음"),
    SUBJECT_MISSING_1013("1013", "SUBJECT 없음"),
    NO_SEND_PERMISSION_1018("1018", "전송 권한 없음"),
    TTL_EXCEEDED_1019("1019", "TTL 초과"),
    CHARSET_ERROR_1020("1020", "Charset conversion error"),
    AUTH_FAILURE_1099("1099", "인증 실패"),
    NO_RECIPIENT_NUMBER_E901("E901", "수신번호가 없는 경우"),
    NO_SUBJECT_E903("E903", "제목 없는 경우"),
    NO_MESSAGE_E904("E904", "메시지가 없는 경우"),
    NO_REPLY_NUMBER_E905("E905", "회신번호가 없는 경우"),
    NO_MESSAGE_KEY_E906("E906", "메시지키가 없는 경우"),
    DUPLICATE_MESSAGE_E915("E915", "중복 메시지"),
    BLOCKED_BY_AUTH_SERVER_E916("E916", "인증서버 차단번호"),
    BLOCKED_BY_CUSTOMER_DB_E917("E917", "고객 DB 차단번호"),
    USER_CALLBACK_FAIL_E918("E918", "USER CALLBACK FAIL"),
    RESEND_FORBIDDEN_E919("E919", "메시지 재발송 금지"),
    INVALID_SERVICE_TYPE_E920("E920", "잘못된 서비스 타입"),
    TEMPLATE_MISMATCH_E921("E921", "버튼 템플릿 매칭 오류"),
    OTHER_ERROR_E999("E999", "기타 오류");

    private final String code;
    private final String message;

    public static void printMessageAboutCode(Long idx, String code) {
        for (KproResultResponseManager kproResultResponseManager : values()) {
            if (kproResultResponseManager.getCode().equals(code)) {
                System.out.println("☆☆☆ [ IDX - " + idx + " | CODE - " + code + " ] : " + kproResultResponseManager.getMessage());
                return;
            }
        }
        throw new IllegalArgumentException("Unknown rslt_code: " + code);
    }



}
