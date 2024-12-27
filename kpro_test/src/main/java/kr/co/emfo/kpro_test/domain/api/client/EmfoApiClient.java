package kr.co.emfo.kpro_test.domain.api.client;

import kr.co.emfo.kpro_test.domain.api.dto.emfoRequest;
import kr.co.emfo.kpro_test.global.config.EmfoClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "emfoClient", url = "http://emfohttp.co.kr", configuration = EmfoClientConfig.class)
public interface EmfoApiClient {

    @PostMapping("/send/send.emfo")
    String sendMessage(@RequestBody emfoRequest.SendMessageDto request);

    @GetMapping("/result_log")
    String getLogNpro(emfoRequest.LogDto request);
}
