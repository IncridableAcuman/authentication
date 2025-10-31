package com.app.backend.exceptions;

public class BadRequestExceptionHandler extends RuntimeException{
    public BadRequestExceptionHandler(String message){
        super(message);
    }
}
