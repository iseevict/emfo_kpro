package kr.co.emfo.kpro_test.domain.message.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.co.emfo.kpro_test.domain.message.service.MessageService;
import kr.co.emfo.kpro_test.domain.message.service.NproMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/messages")
public class MessageController {

    private final MessageService messageService;
    private final NproMessageService nproMessageService;

    @GetMapping("/logs/{idx}")
    @Operation(summary = "상세 로그 확인")
    public void getLog(@PathVariable("idx") Long i) {
        messageService.getLog(i);
    }

    @GetMapping("/logs")
    @Operation(summary = "전체 로그 확인")
    public void getLogs() {
        messageService.getLogs();
    }

    @GetMapping("/log")
    @Operation(summary = "npro 로그")
    public void getLogsNpro(@RequestParam("mIdx") Long mIdx, @RequestParam("mId") String mId) { nproMessageService.getLogs(mIdx, mId); }
}
