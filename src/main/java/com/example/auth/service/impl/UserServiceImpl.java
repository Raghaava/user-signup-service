package com.example.auth.service.impl;

import com.example.auth.io.entity.UserEntity;
import com.example.auth.io.repository.UserRepository;
import com.example.auth.service.UserService;
import com.example.auth.service.exception.UserServiceException;
import com.example.auth.shared.Utils;
import com.example.auth.shared.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntityFromDB = userRepository.findByEmail(userDto.getEmail());

        if(userEntityFromDB != null)
        {
            throw new UserServiceException(String.format("User with email %s already exists.", userDto.getEmail()));
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setUserId(Utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userEntityFromDB = userRepository.save(userEntity);
        return modelMapper.map(userEntityFromDB, UserDto.class);
    }

    @Override
    public UserDto getUser(String userName) {
        UserEntity userEntityFromDB = userRepository.findByEmail(userName);

        if(userEntityFromDB == null)
        {
            throw new UsernameNotFoundException(String.format("User with name %s not found.", userEntityFromDB));
        }

        return modelMapper.map(userEntityFromDB, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntityFromDB = userRepository.findByEmail(userName);

        if(userEntityFromDB == null)
        {
            throw new UsernameNotFoundException(String.format("User with name %s not found.", userEntityFromDB));
        }

        return new User(userName, userEntityFromDB.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
