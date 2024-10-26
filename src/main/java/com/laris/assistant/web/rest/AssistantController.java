package com.laris.assistant.web.rest;

import com.laris.assistant.service.openai.OpenAiService;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/api/public")
public class AssistantController {

    private final OpenAiService openAiService;

    public AssistantController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/assistant/ask")
    public Mono<String> askAssistant(@RequestHeader Map<String, String> headers, @RequestParam Map<String, String> parameters) {
        log.debug("Logging headers...");
        headers.forEach((k, v) -> log.info("header {}: {}", k, v));
        log.debug("Logging params...");
        parameters.forEach((k, v) -> log.info("param {}: {}", k, v));
        String ask = parameters.getOrDefault("Body", "hello");
        log.debug("ask: {}", ask);
        return openAiService.getAssistantResponse(ask);
    }
}
