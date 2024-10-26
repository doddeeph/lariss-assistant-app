package com.laris.assistant.service.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiRequest {

    private String model;
    private List<Message> messages;

    @JsonProperty("max_completion_tokens")
    private int maxCompletionTokens;

    private double temperature;
}
