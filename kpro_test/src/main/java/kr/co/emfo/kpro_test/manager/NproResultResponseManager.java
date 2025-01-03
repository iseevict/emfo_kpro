package kr.co.emfo.kpro_test.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NproResultResponseManager {
    SUCCESS_0("0", "성공"),
    TIMEOUT_1("1", "TIMEOUT"),
    PROCESSING_A("A", "핸드폰 호 처리 중"),
    SHADOW_AREA_B("B", "음영지역"),
    POWER_OFF_C("C", "Power off"),
    STORAGE_LIMIT_D("D", "메시지 저장개수 초과"),
    INVALID_NUMBER_2("2", "잘못된 전화번호"),
    TEMPORARY_SERVICE_STOP_a("a", "일시 서비스 정지"),
    DEVICE_ISSUE_b("b", "기타 단말기 문제"),
    CALL_REJECTION_c("c", "착신 거절"),
    OTHER_d("d", "기타"),
    CARRIER_FORMAT_ERROR_e("e", "이통사 SMC 형식 오류"),
    SPAM_FILTERED_s("s", "메시지 스팸차단"),
    SPAM_RECEIVER_n("n", "수신번호 스팸차단"),
    SPAM_REPLY_r("r", "회신번호 스팸차단"),
    SPAM_BLOCKED_t("t", "스팸차단"),
    OTHER_ERROR_z("z", "기타"),
    FORMAT_ERROR_f("f", "엠포 자체 형식 오류"),
    DEVICE_UNAVAILABLE_g("g", "서비스 불가 단말기"),
    PROCESSING_FAILED_h("h", "핸드폰 호 처리 불가"),
    DELETED_BY_OPERATOR_i("i", "SMC 운영자가 메시지 삭제"),
    CARRIER_QUEUE_FULL_j("j", "이통사 내부 메시지 Que Full"),
    SPAM_BY_CARRIER_k("k", "이통사에서 스팸"),
    SPAM_BY_EMFO_l("l", "엠포에서 스팸"),
    COUNT_LIMIT_n("n", "건수 제한"),
    MESSAGE_LENGTH_LIMIT_o("o", "메시지 길이 제한"),
    INVALID_PHONE_FORMAT_p("p", "폰 번호가 형식에 어긋난 경우"),
    INVALID_FIELD_FORMAT_q("q", "필드 형식이 잘못된 경우"),
    MMS_CONTENT_MISSING_X("X", "MMS 콘텐트를 참조할 수 없음"),
    DUPLICATE_KEY_q("q", "메시지 중복키 체크"),
    DAILY_LIMIT_y("y", "하루에 한 수신번호에 보낼 수 있는 메시지 수량 초과"),
    KEYWORD_MISSING_w("w", "특정 키워드 없으면 스팸"),

    NORMAL_REQUEST_100("100", "정상요청(실제발송)"),
    NORMAL_TEST_REQUEST_101("101", "정상요청(테스트발송)"),
    UNREGISTERED_SENDER_201("201", "미등록 발신번호"),
    DAILY_LIMIT_SMS_301("301", "하루 전송가능건수 초과(SMS)"),
    DAILY_LIMIT_LMS_302("302", "하루 전송가능건수 초과(LMS)"),
    DAILY_LIMIT_MMS_303("303", "하루 전송가능건수 초과(MMS)"),
    INSUFFICIENT_BALANCE_PREPAID_401("401", "잔액부족(선불)"),
    INSUFFICIENT_BALANCE_POSTPAID_402("402", "잔액부족(후불)"),
    ACCOUNT_MISMATCH_501("501", "계정정보 미일치"),
    ACCOUNT_ATTEMPTS_EXCEEDED_502("502", "계정정보 미일치 횟수 초과"),
    SMS_BLOCKED_601("601", "SMS발송차단"),
    LMS_BLOCKED_602("602", "LMS발송차단"),
    MMS_BLOCKED_603("603", "MMS발송차단"),
    UNREGISTERED_DOMAIN_701("701", "미등록 도메인"),
    DUPLICATE_SERIAL_801("801", "일련번호 중복등록"),
    OTHER_ERRORS_901_999("900", "기타 오류 범위");

    private final String code;
    private final String message;

    public static void printMessageAboutCode(Long idx, String code) {
        for (NproResultResponseManager nproResultResponseManager : values()) {
            if (nproResultResponseManager.getCode().equals(code)) {
                System.out.println("★★★ [ IDX - " + idx + " | CODE - " + code + " ] : " + nproResultResponseManager.getMessage());
                return;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
