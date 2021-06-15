package com.saodevblog.app.ws.ui.controller;

import com.saodevblog.app.ws.service.UserService;
import com.saodevblog.app.ws.shared.dto.UserDto;
import com.saodevblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.saodevblog.app.ws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") // http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    /**
     *  Get a specific user with their id
     *
     * @return returns user with specified id
      */
    @GetMapping
    public String getUser() {
        return "get user was called";
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnVal = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnVal);
        return returnVal;
    }

    @PutMapping
    public  String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }
}
