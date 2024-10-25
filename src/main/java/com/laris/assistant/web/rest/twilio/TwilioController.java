package com.laris.assistant.web.rest.twilio;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/twilio")
@Log4j2
public class TwilioController {

    @PostMapping("/webhook")
    public String webhook(HttpServletRequest request, @RequestParam Map<String, String> params) {
        log.info("Logging ala headers");
        request.getHeaderNames().asIterator().forEachRemaining(name -> log.info("{}: {}", name, request.getHeader(name)));
        log.info("Logging all parameters");
        params.forEach((key, value) -> log.info("{}: {}", key, value));
        return "Webhook received and logged!";
    }
}
