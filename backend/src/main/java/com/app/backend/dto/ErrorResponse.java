package com.app.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime time;
    public ErrorResponse(int status,String error,String message,String path,LocalDateTime time){
        this.status=status;
        this.error=error;
        this.message=message;
        this.path=path;
        this.time=time;
    }
}
