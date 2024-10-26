package com.laris.assistant.service.twilio;

import com.laris.assistant.service.openai.OpenAiService;
import com.laris.assistant.service.twilio.dto.TwilioWebhookRequestHeader;
import com.laris.assistant.service.twilio.dto.TwilioWebhookRequestParam;
import com.twilio.security.RequestValidator;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class TwilioService {

    private final OpenAiService openAiService;

    @Value("${twilio.auth-token}")
    private String authToken;

    public TwilioService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public Mono<String> webhook(String requestUrl, TwilioWebhookRequestHeader requestHeader, TwilioWebhookRequestParam requestParam) {
        return Mono
            .just(isRequestValid(requestUrl, requestParam.getRequestParam(), requestHeader.getTwilioSignature()))
            .flatMap(isValid -> {
                if (isValid) {
                    return openAiService.getAssistantResponse(requestParam.getBody());
                } else {
                    return Mono.just("Invalid request signature!");
                }
            });
    }

    private boolean isRequestValid(String url, Map<String, String> params, String twilioSignature) {
        return new RequestValidator(authToken).validate(url, params, twilioSignature);
    }
}
