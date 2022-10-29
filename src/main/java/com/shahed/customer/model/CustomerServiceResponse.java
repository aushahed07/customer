package com.shahed.customer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerServiceResponse {

    private Boolean hasError;
    private String decentMessage;
    private String errorDetails;
    private String content;

    public CustomerServiceResponse() {
    }

    public CustomerServiceResponse(String decentMessage, String errorDetails) {
        this.hasError = Boolean.TRUE;
        this.decentMessage = decentMessage;
        this.errorDetails = errorDetails;
    }

    public CustomerServiceResponse(String message) {
        this.hasError = Boolean.FALSE;
        this.decentMessage = message;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            this.content = objectMapper.writeValueAsString(Boolean.TRUE.toString());
        } catch (JsonProcessingException var4) {
            var4.printStackTrace();
        }
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getDecentMessage() {
        return decentMessage;
    }

    public void setDecentMessage(String decentMessage) {
        this.decentMessage = decentMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
