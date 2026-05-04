package edu.cit.chan.restoradar.shared.dto;

import java.time.Instant;

public class ErrorResponse {
    private boolean success;
    private Object data;
    private ErrorDetail error;
    private Instant timestamp;

    public ErrorResponse() {}

    public ErrorResponse(boolean success, Object data, ErrorDetail error, Instant timestamp) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = timestamp;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public ErrorDetail getError() { return error; }
    public void setError(ErrorDetail error) { this.error = error; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public static class ErrorDetail {
        private String code;
        private String message;
        private Object details;

        public ErrorDetail() {}

        public ErrorDetail(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public Object getDetails() { return details; }
        public void setDetails(Object details) { this.details = details; }
    }
}