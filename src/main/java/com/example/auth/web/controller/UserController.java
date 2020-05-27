package com.example.auth.web.controller;

import com.example.auth.service.UserService;
import com.example.auth.shared.dto.UserDto;
import com.example.auth.web.request.UserDetailsRequestModel;
import com.example.auth.web.response.UserRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userRequestModel) {
        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto savedUser = userService.createUser(userDto);
        return modelMapper.map(savedUser, UserRest.class);
    }

    @GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String getUser(@PathVariable long userId) {
        return "get user.";
    }

    @PutMapping
    public String updateUser() {
        return "update user.";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user.";
    }
}
