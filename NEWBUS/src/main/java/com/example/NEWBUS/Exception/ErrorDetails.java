package com.example.NEWBUS.Exception;

import java.time.LocalDateTime;

public class ErrorDetails {
    //give timeStump
    private LocalDateTime localDateTime;
    private String message;
    private String path;
    private String statusCode;

    public ErrorDetails() {
    }

    public ErrorDetails(LocalDateTime localDateTime, String message, String path, String statusCode) {
        this.localDateTime = localDateTime;
        this.message = message;
        this.path = path;
        this.statusCode = statusCode;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}