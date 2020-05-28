package com.example.auth.shared.dto;

import lombok.Data;

@Data
public class AddressDto {
    private long id;
    private String addressId;
    private String street;
    private String city;
    private UserDto user;
}
