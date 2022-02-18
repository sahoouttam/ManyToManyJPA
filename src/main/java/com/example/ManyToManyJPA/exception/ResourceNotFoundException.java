package com.example.ManyToManyJPA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String cause) {
        super(cause);
    }
}
