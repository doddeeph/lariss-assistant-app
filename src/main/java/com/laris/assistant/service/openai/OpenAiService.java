package com.laris.assistant.service.openai;

import com.laris.assistant.service.openai.dto.Message;
import com.laris.assistant.service.openai.dto.OpenAiRequest;
import com.laris.assistant.service.openai.dto.OpenAiResponse;
import com.laris.assistant.service.openai.dto.Role;
import java.util.Arrays;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class OpenAiService {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.model}")
    private String model;

    @Value("${openai.api.max-token}")
    private int maxToken;

    @Value("${openai.api.temperature}")
    private double temperature;

    @Value("${openai.api.role-system-message}")
    private String roleSystemMessage;

    public OpenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private OpenAiRequest buildOpenAiRequest(String ask) {
        return OpenAiRequest
            .builder()
            .model(model)
            .messages(
                Arrays.asList(
                    Message.builder().role(Role.SYSTEM.name().toLowerCase()).content(roleSystemMessage).build(),
                    Message.builder().role(Role.USER.name().toLowerCase()).content(ask).build()
                )
            )
            .maxCompletionTokens(maxToken)
            .temperature(temperature)
            .build();
    }

    public Mono<String> getAssistantResponse(String ask) {
        return webClient
            .post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .bodyValue(buildOpenAiRequest(ask))
            .retrieve()
            .onStatus(
                HttpStatus::is4xxClientError,
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Client error " + errorBody)))
            )
            .onStatus(
                HttpStatus::is5xxServerError,
                clientResponse ->
                    clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Server error: " + errorBody)))
            )
            .bodyToMono(OpenAiResponse.class)
            .map(openAiResponse -> openAiResponse.getChoices().get(0).getMessage().getContent())
            .doOnError(WebClientResponseException.class, e -> log.error("Error occurred: {}", e.getResponseBodyAsString()));
    }
}
