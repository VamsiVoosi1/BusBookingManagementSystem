package com.example.NEWBUS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends RuntimeException{
    private String recourseName;
    private String fieldName;
    private Long fieldValue;

    public ResourceNotFoundException(String recourseName, String fieldName, Long fieldValue) {
        super(String.format("%sNot found with %s :'%s'",recourseName,fieldName,fieldValue));
        this.recourseName = recourseName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}