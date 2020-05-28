package com.example.auth.service.impl;

import com.example.auth.exception.UserServiceException;
import com.example.auth.io.entity.PasswordResetRequestEntity;
import com.example.auth.io.entity.UserEntity;
import com.example.auth.io.repository.PasswordResetRequestRepository;
import com.example.auth.io.repository.UserRepository;
import com.example.auth.service.UserService;
import com.example.auth.shared.AmazonSES;
import com.example.auth.shared.Utils;
import com.example.auth.shared.dto.UserDto;
import com.example.auth.web.request.PasswordResetRequest;
import com.example.auth.web.request.UserUpdateRequestModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserEntity userEntityFromDB = userRepository.findByEmail(userDto.getEmail());

        if (userEntityFromDB != null) {
            throw new UserServiceException(String.format("User with email %s already exists.", userDto.getEmail()));
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setUserId(Utils.generateUserId(30));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        userEntity.getAddresses().stream().forEach(
                address -> {
                    address.setAddressId(Utils.generateUserId(30));
                    address.setUserDetails(userEntity);
                }
        );
        userEntity.setEmailVerificationStatus(false);
        userEntity.setEmailVerificationToken(Utils.generateToken(userEntity.getUserId()));
        userEntityFromDB = userRepository.save(userEntity);
        userDto = modelMapper.map(userEntityFromDB, UserDto.class);
        new AmazonSES().sendVerificationEmail(userDto);
        return userDto;
    }

    @Override
    public UserDto getUser(String userName) {
        UserEntity userEntityFromDB = userRepository.findByEmail(userName);

        if (userEntityFromDB == null) {
            throw new UsernameNotFoundException(String.format("User with a name %s not found.", userName));
        }

        return modelMapper.map(userEntityFromDB, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntityFromDB = userRepository.findByUserId(userId);

        if (userEntityFromDB == null) {
            throw new UsernameNotFoundException(String.format("User with an id %s not found.", userId));
        }

        return modelMapper.map(userEntityFromDB, UserDto.class);
    }

    @Override
    public UserDto updateUser(String userId, UserUpdateRequestModel userUpdateRequestModel) {
        UserEntity userEntityFromDB = userRepository.findByUserId(userId);

        if (userEntityFromDB == null) {
            throw new UsernameNotFoundException(String.format("User with an id %s not found.", userId));
        }
        userEntityFromDB.setFirstName(userUpdateRequestModel.getFirstName());
        userEntityFromDB.setLastName(userUpdateRequestModel.getLastName());

        userEntityFromDB = userRepository.save(userEntityFromDB);
        return modelMapper.map(userEntityFromDB, UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity userEntityFromDB = userRepository.findByUserId(userId);

        if (userEntityFromDB == null) {
            throw new UsernameNotFoundException(String.format("User with an id %s not found.", userId));
        }

        userRepository.delete(userEntityFromDB);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<UserEntity> UserEntitiesPage = userRepository.findAll(pageRequest);
        List<UserEntity> userEntityList = UserEntitiesPage.getContent();
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return modelMapper.map(userEntityList, type);
    }

    @Override
    public boolean verifyEmail(String token) {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        if (userEntity == null
                || userEntity.isEmailVerificationStatus()
                || Utils.isTokenValid(token, userEntity.getUserId())) {
            return false;
        }
        userEntity.setEmailVerificationToken(null);
        userEntity.setEmailVerificationStatus(true);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public boolean createPasswordResetRequest(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return false;
        }
        String token = Utils.generateToken(userEntity.getUserId());
        PasswordResetRequestEntity passwordResetRequestEntity = new PasswordResetRequestEntity();
        passwordResetRequestEntity.setPasswordResetRequestToken(token);
        passwordResetRequestEntity.setUserDetails(userEntity);
        passwordResetRequestRepository.save(passwordResetRequestEntity);
        new AmazonSES().sendPasswordResetEmail(userEntity.getEmail(), token);
        return true;
    }

    @Override
    public boolean resetPassword(PasswordResetRequest passwordResetRequest) {
        PasswordResetRequestEntity passwordResetRequestEntity = passwordResetRequestRepository
                .findByPasswordResetRequestToken(passwordResetRequest.getToken());

        if(passwordResetRequestEntity == null) {
            return false;
        }

        String encodedPassword = bCryptPasswordEncoder.encode(passwordResetRequest.getPassword());
        UserEntity userEntity = passwordResetRequestEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        UserEntity userEntityFromDB = userRepository.save(userEntity);
        passwordResetRequestRepository.delete(passwordResetRequestEntity);
        return userEntityFromDB.getEncryptedPassword() == userEntity.getEncryptedPassword();
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntityFromDB = userRepository.findByEmail(userName);

        if (userEntityFromDB == null) {
            throw new UsernameNotFoundException(String.format("User with a name %s not found.", userName));
        }

        return new User(userName, userEntityFromDB.getEncryptedPassword(), userEntityFromDB.isEmailVerificationStatus(), true, true, true, new ArrayList<>());
    }
}
