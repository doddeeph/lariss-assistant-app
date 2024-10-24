package com.laris.assistant.web.rest.openai;

import com.laris.assistant.service.openai.OpenAiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/public/openai")
public class OpenAiController {

    private final OpenAiService openAiService;

    public OpenAiController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/assistant")
    public Mono<String> askAssistant(@RequestParam String ask) {
        return openAiService.getAssistantResponse(ask);
    }
}
