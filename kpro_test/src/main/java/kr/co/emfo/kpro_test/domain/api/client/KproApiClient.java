package kr.co.emfo.kpro_test.domain.api.client;

import jakarta.validation.Valid;
import kr.co.emfo.kpro_test.domain.api.dto.KproApiRequest;
import kr.co.emfo.kpro_test.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "KproApiClient", url = "https://kko.emfo-api.co.kr:8080", configuration = FeignClientConfig.class)
@Validated
public interface KproApiClient {

    @PostMapping("/rest/kakao/send")
    String sendKproMessage(@RequestBody @Valid  List<KproApiRequest.SendKproMessageRequestDto> requestList);

    @GetMapping("/rest/kakao/logs")
    String getKproLog(
            @RequestParam("idx") Long idx
    );

    @GetMapping("/rest/kakao/logs")
    String getKproLogs();
}
