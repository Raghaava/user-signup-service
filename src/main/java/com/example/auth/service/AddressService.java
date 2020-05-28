package com.example.auth.service;

import com.example.auth.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto getAddress(String addressId);

    List<AddressDto> getAddresses(String userId);
}
