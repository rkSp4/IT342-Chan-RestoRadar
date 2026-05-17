package edu.cit.chan.restoradar.shared.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse {

    private boolean success;
    private Object data;
    private ErrorBody error;
    private String timestamp;

    public ApiResponse() {}

    public ApiResponse(boolean success, Object data, ErrorBody error, String timestamp) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.timestamp = timestamp;
    }

    public static ApiResponse success(Object data) {
        return new ApiResponse(true, data, null, Instant.now().toString());
    }

    public static ApiResponse error(String code, String message, Object details) {
        return new ApiResponse(false, null, new ErrorBody(code, message, details), Instant.now().toString());
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public ErrorBody getError() { return error; }
    public void setError(ErrorBody error) { this.error = error; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public static class ErrorBody {
        private String code;
        private String message;
        private Object details;

        public ErrorBody() {}

        public ErrorBody(String code, String message, Object details) {
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