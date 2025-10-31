package com.app.backend.exceptions;

public class NotFoundExceptionHandler extends RuntimeException{
    public NotFoundExceptionHandler(String message){
        super(message);
    }
}
