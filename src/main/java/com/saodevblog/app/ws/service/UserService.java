package com.saodevblog.app.ws.service;

import com.saodevblog.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserByUserId(String userId);
    UserDto updateUser(String userId, UserDto details);
    void deleteUser(String userId);
    List<UserDto> getUsers(int page, int limit);
}
