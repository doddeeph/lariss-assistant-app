package com.laris.assistant.service.openai.dto;

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
    private int maxToken;
    private double temperature;
}
