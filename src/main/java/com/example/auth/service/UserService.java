package com.example.auth.service;

import com.example.auth.shared.dto.UserDto;
import com.example.auth.web.request.PasswordResetRequest;
import com.example.auth.web.request.UserUpdateRequestModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUser(String userName);

    UserDto getUserByUserId(String userId);

    UserDto updateUser(String userId, UserUpdateRequestModel userUpdateRequestModel);

    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);

    boolean verifyEmail(String token);

    boolean createPasswordResetRequest(String email);

    boolean resetPassword(PasswordResetRequest passwordResetRequest);
}
