package com.example.auth.web.response;

import lombok.Data;

import java.util.List;

@Data
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<AddressRest> addresses;
}
