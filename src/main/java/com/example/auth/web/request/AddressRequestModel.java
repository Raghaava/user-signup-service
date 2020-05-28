package com.example.auth.web.request;

import lombok.Data;

@Data
public class AddressRequestModel {
    private String addressId;
    private String street;
    private String city;
}
