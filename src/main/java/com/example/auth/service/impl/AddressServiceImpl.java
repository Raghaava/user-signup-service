package com.example.auth.service.impl;

import com.example.auth.exception.UserServiceException;
import com.example.auth.io.entity.AddressEntity;
import com.example.auth.io.entity.UserEntity;
import com.example.auth.io.repository.AddressRepository;
import com.example.auth.io.repository.UserRepository;
import com.example.auth.service.AddressService;
import com.example.auth.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public AddressDto getAddress(String addressId) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        return modelMapper.map(addressEntity, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAddresses(String userId) {
        UserEntity userEntityFromDB = userRepository.findByUserId(userId);
        if (userEntityFromDB == null) {
            throw new UserServiceException(String.format("User with userId %s doesn't exists.", userId));
        }
        List<AddressEntity> addresses  = addressRepository.findByUserDetails(userEntityFromDB);
        Type type = new TypeToken<List<AddressDto>>(){}.getType();
        return modelMapper.map(addresses, type);
    }
}
