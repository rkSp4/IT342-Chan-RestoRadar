package edu.cit.chan.restoradar.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;

/**
 * Wraps every API response in the envelope defined in the SDD:
 *
 * Success: { "success": true,  "data": {...}, "error": null, "timestamp": "..." }
 * Error:   { "success": false, "data": null,  "error": {...}, "timestamp": "..." }
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse {

    private boolean success;
    private Object data;
    private ErrorBody error;
    private String timestamp;

    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .success(true)
                .data(data)
                .error(null)
                .timestamp(Instant.now().toString())
                .build();
    }

    public static ApiResponse error(String code, String message, Object details) {
        return ApiResponse.builder()
                .success(false)
                .data(null)
                .error(new ErrorBody(code, message, details))
                .timestamp(Instant.now().toString())
                .build();
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class ErrorBody {
        private String code;
        private String message;
        private Object details;
    }
}