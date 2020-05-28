package com.example.auth.web.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String token;
    private String password;
}
