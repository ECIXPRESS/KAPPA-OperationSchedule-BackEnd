package edu.dosw.KAPPA_OperationSchedule_BackEnd.Exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private String errorCode;
    private String message;
    private String path;

    public ErrorResponse(String errorCode, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public String getPath() { return path; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setMessage(String message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
}