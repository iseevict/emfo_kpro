package kr.co.emfo.kpro_test.domain.api.client;

import kr.co.emfo.kpro_test.domain.api.dto.emfoRequest;
import kr.co.emfo.kpro_test.global.config.EmfoClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@FeignClient(name = "emfoClient", url = "http://emfohttp.co.kr", configuration = EmfoClientConfig.class)
public interface EmfoApiClient {

    @PostMapping(value = "/send/send.emfo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String sendMessage(@RequestPart("m_idx") Long mIdx,
                       @RequestPart("m_id") String mId,
                       @RequestPart("m_pwd") String mPwd,
                       @RequestPart("call_to") String callTo,
                       @RequestPart("call_from") String callFrom,
                       @RequestPart("m_subject") String mSubject,
                       @RequestPart("m_message") String mMessage,
                       @RequestPart("m_type") String mType,
                       @RequestPart("m_send_type") String mSendType,
                       @RequestPart("m_file_name") String mFileName,
                       @RequestPart("m_yyyy") String mYyyy,
                       @RequestPart("m_mm") String mMm,
                       @RequestPart("m_dd") String mDd,
                       @RequestPart("m_hh") String mHh,
                       @RequestPart("m_mi") String mMi,
                       @RequestPart("url_success") String urlSuccess,
                       @RequestPart("url_fail") String urlFail,
                       @RequestPart("flag_test") String flagTest,
                       @RequestPart("flag_deny") String flagDeny,
                       @RequestPart("flag_merge") String flagMerge);

    @GetMapping("/result_log")
    String getLogNpro(emfoRequest.LogDto request);
}
