package com.laris.assistant.service.twilio.dto;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@NoArgsConstructor
public class TwilioWebhookRequestHeader {

    private String twilioSignature;

    public TwilioWebhookRequestHeader(Map<String, String> requestHeader) {
        requestHeader.forEach((k, v) -> log.debug("req header {}: {}", k, v));
        this.twilioSignature = requestHeader.getOrDefault("X-Twilio-Signature", "");
    }
}
