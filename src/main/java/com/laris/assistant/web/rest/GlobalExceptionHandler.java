package com.laris.assistant.web.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.OK).body("An error occurred " + e.getMessage());
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        String errMsg;
        switch (e.getStatusCode()) {
            case UNAUTHORIZED:
                errMsg = "Authentication error: Please check your API key.";
                break;
            case TOO_MANY_REQUESTS:
                errMsg = "Rate limit exceeded: Please slow down your requests.";
                break;
            default:
                errMsg = "Error occurred: " + e.getResponseBodyAsString();
                break;
        }
        log.error("Error occurred: {}", errMsg);
        return ResponseEntity.status(HttpStatus.OK).body(errMsg);
    }
}
