package com.app.backend.exceptions;

public class UnAuthorizeExceptionHandler extends RuntimeException{
    public UnAuthorizeExceptionHandler(String message){
        super(message);
    }
}
