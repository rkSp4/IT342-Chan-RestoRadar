package edu.cit.chan.restoradar.dto;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private boolean success;
    private Object data;
    private ErrorDetail error;
    private Instant timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String code;
        private String message;
        private Object details;
    }
}