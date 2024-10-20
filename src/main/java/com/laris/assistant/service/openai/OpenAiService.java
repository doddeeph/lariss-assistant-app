package com.laris.assistant.service.openai;

import com.laris.assistant.service.openai.dto.Message;
import com.laris.assistant.service.openai.dto.OpenAiRequest;
import com.laris.assistant.service.openai.dto.OpenAiResponse;
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

    // Inject the API key from the environment variable OPENAI_API_KEY
    @Value("${OPENAI_API_KEY}")
    //@Value("${openai.api.key}")
    private String apiKey;

    // Inject the API key from the environment variable OPENAI_API_URL
    @Value("${OPENAI_API_URL}")
    //@Value("${openai.api.url}")
    private String apiUrl;

    public OpenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    private OpenAiRequest buildOpenAiRequest(String userMessage) {
        return OpenAiRequest
            .builder()
            .model("gpt-3.5-turbo")
            .messages(
                Arrays.asList(
                    Message.builder().role("system").content("You are a helpful assistant.").build(),
                    Message.builder().role("user").content(userMessage).build()
                )
            )
            .maxToken(1000)
            .temperature(0.7)
            .build();
    }

    public Mono<String> getAssistantResponse(String userMessage) {
        return webClient
            .post()
            .uri(apiUrl)
            .header("Authorization", "Bearer " + apiKey)
            .bodyValue(buildOpenAiRequest(userMessage))
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
            .doOnError(
                WebClientResponseException.class,
                e -> {
                    log.error("Error occurred: {}", e.getResponseBodyAsString());
                }
            );
    }
}
