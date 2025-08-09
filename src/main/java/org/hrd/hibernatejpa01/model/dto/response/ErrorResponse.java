package org.hrd.hibernatejpa01.model.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private Boolean success;
    private int status;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();
}
