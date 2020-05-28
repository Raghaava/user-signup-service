package com.example.auth.web.controller;

import com.example.auth.service.AddressService;
import com.example.auth.service.UserService;
import com.example.auth.shared.dto.AddressDto;
import com.example.auth.shared.dto.UserDto;
import com.example.auth.web.request.PasswordResetRequest;
import com.example.auth.web.request.UserDetailsRequestModel;
import com.example.auth.web.request.UserUpdateRequestModel;
import com.example.auth.web.response.AddressRest;
import com.example.auth.web.response.OperationName;
import com.example.auth.web.response.OperationStatus;
import com.example.auth.web.response.OperationStatusResponseModel;
import com.example.auth.web.response.UserRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userRequestModel) {
        UserDto userDto = modelMapper.map(userRequestModel, UserDto.class);
        UserDto savedUser = userService.createUser(userDto);
        return modelMapper.map(savedUser, UserRest.class);
    }

    @GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String userId) {
        UserDto userDto = userService.getUserByUserId(userId);
        return modelMapper.map(userDto, UserRest.class);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "limit", defaultValue = "2") int limit) {
        List<UserDto> userDtos = userService.getUsers(page, limit);
        Type type = new TypeToken<List<UserRest>>(){}.getType();
        return modelMapper.map(userDtos, type);
    }

    @PutMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String userId, @RequestBody UserUpdateRequestModel userUpdateRequestModel) {
        UserDto userDto = userService.updateUser(userId, userUpdateRequestModel);
        return modelMapper.map(userDto, UserRest.class);
    }

    @DeleteMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusResponseModel deleteUser(@PathVariable("userId") String userId) {
        userService.deleteUser(userId);
        OperationStatusResponseModel model = new OperationStatusResponseModel();
        model.setName(OperationName.DELETE_USER.name());
        model.setStatus(OperationStatus.SUCCESS.name());
        return model;
    }

    @GetMapping(path="/{userId}/addresses/{addressId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public AddressRest getUserAddress(@PathVariable("userId") String userId, @PathVariable("addressId")  String addressId) {
        AddressDto addresses = addressService.getAddress(addressId);
        return modelMapper.map(addresses, AddressRest.class);
    }

    @GetMapping(path="/{userId}/addresses", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<AddressRest> getUserAddresses(@PathVariable("userId") String userId) {
        List<AddressDto> addresses = addressService.getAddresses(userId);
        Type type = new TypeToken<List<AddressRest>>(){}.getType();
        return modelMapper.map(addresses, type);
    }

    @GetMapping(path="/email-verification", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusResponseModel verifyEmail(@RequestParam("token") String token) {
        boolean result = userService.verifyEmail(token);
        OperationStatusResponseModel model = new OperationStatusResponseModel();
        model.setName(OperationName.VERIFY_EMAIL.name());
        if(result) {
            model.setStatus(OperationStatus.SUCCESS.name());
        }
        else {
            model.setStatus(OperationStatus.ERROR.name());
        }
        return model;
    }

    @PostMapping(path="/password-reset-request", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusResponseModel passwordResetRequest(@RequestBody String email) {
        boolean result = userService.createPasswordResetRequest(email);
        OperationStatusResponseModel model = new OperationStatusResponseModel();
        model.setName(OperationName.PASSWORD_RESET_REQUEST.name());
        if(result) {
            model.setStatus(OperationStatus.SUCCESS.name());
        }
        else {
            model.setStatus(OperationStatus.ERROR.name());
        }
        return model;
    }

    @PostMapping(path="/password-reset", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusResponseModel passwordResetRequest(@RequestBody PasswordResetRequest passwordResetRequest) {
        boolean result = userService.resetPassword(passwordResetRequest);
        OperationStatusResponseModel model = new OperationStatusResponseModel();
        model.setName(OperationName.PASSWORD_RESET.name());
        if(result) {
            model.setStatus(OperationStatus.SUCCESS.name());
        }
        else {
            model.setStatus(OperationStatus.ERROR.name());
        }
        return model;
    }
}
