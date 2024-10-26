package com.laris.assistant.web.rest.twilio;

import com.laris.assistant.service.twilio.TwilioService;
import com.laris.assistant.service.twilio.dto.TwilioWebhookRequestHeader;
import com.laris.assistant.service.twilio.dto.TwilioWebhookRequestParam;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/public/twilio")
public class TwilioController {

    private final TwilioService twilioService;

    public TwilioController(TwilioService twilioService) {
        this.twilioService = twilioService;
    }

    @GetMapping("/webhook")
    public Mono<String> webhook(
        HttpServletRequest request,
        @RequestHeader Map<String, String> requestHeader,
        @RequestParam Map<String, String> requestParam
    ) {
        return twilioService.webhook(
            request.getRequestURL().toString(),
            new TwilioWebhookRequestHeader(requestHeader),
            new TwilioWebhookRequestParam(requestParam)
        );
    }
}
