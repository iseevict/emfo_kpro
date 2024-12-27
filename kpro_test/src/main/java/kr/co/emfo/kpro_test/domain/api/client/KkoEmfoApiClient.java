package kr.co.emfo.kpro_test.domain.api.client;

import kr.co.emfo.kpro_test.domain.api.dto.KkoEmfoRequest;
import kr.co.emfo.kpro_test.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "kkoEmfoClient", url = "https://kko.emfo-api.co.kr:8080", configuration = FeignClientConfig.class)
public interface KkoEmfoApiClient {

    @PostMapping("/rest/kakao/send")
    String sendMessage(@RequestBody List<KkoEmfoRequest.SendMessageDto> requestList);

    @GetMapping("/rest/kakao/logs")
    String getLog(
            @RequestParam("idx") Long idx
    );

    @GetMapping("/rest/kakao/logs")
    String getLogs();
}
