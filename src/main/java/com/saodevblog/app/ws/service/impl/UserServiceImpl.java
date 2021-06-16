package com.saodevblog.app.ws.service.impl;

import com.saodevblog.app.ws.exception.UserServiceException;
import com.saodevblog.app.ws.io.repositories.UserRepository;
import com.saodevblog.app.ws.io.entity.UserEntity;
import com.saodevblog.app.ws.service.UserService;
import com.saodevblog.app.ws.shared.Utils;
import com.saodevblog.app.ws.shared.dto.UserDto;
import com.saodevblog.app.ws.ui.model.response.ErrorMessages;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto user) {


        if(userRepo.findUserByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity storedUser = userRepo.save(userEntity);

        UserDto returnVal = new UserDto();
        BeanUtils.copyProperties(storedUser, returnVal);
        return returnVal;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findUserByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUser(String email) {
        UserDto userDto = new UserDto();
        UserEntity userEntity = userRepo.findUserByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity entity = userRepo.findByUserId(userId);

        if(entity == null) throw new UsernameNotFoundException("User with id '" + userId + "' not found");

        UserDto dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto details) {
        UserDto returnVal = new UserDto();

        UserEntity entity = userRepo.findByUserId(userId);

        if(entity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        entity.setFirstName(details.getFirstName());
        entity.setLastName(details.getLastName());
        userRepo.save(entity);

        BeanUtils.copyProperties(entity, returnVal);

        return returnVal;
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity entity = userRepo.findByUserId(userId);

        if(entity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepo.delete(entity);
    }
}
