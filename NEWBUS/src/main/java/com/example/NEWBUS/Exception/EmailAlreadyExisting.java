package com.example.NEWBUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class EmailAlreadyExisting extends RuntimeException{
    private String message;

    public EmailAlreadyExisting(String message) {
        super(message);//message to parent
        this.message = message;
    }
}