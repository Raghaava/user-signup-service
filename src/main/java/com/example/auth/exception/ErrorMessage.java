package com.example.auth.exception;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorMessage {
    private Date timeStamp;
    private String message;
}
