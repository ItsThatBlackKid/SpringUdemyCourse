package com.saodevblog.app.ws.ui.controller;

import com.saodevblog.app.ws.exception.UserServiceException;
import com.saodevblog.app.ws.service.UserService;
import com.saodevblog.app.ws.shared.dto.UserDto;
import com.saodevblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.saodevblog.app.ws.ui.model.response.ErrorMessages;
import com.saodevblog.app.ws.ui.model.response.OperationStatusModel;
import com.saodevblog.app.ws.ui.model.response.RequestOperationStatus;
import com.saodevblog.app.ws.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @GetMapping(path="/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest returnVal = new UserRest();

        UserDto dto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(dto,returnVal);

        return returnVal;
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        if(userDetails.getEmail() == null || userDetails.getEmail().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

        UserRest returnVal = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnVal);
        return returnVal;
    }

    @PutMapping(path="/{id}")
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnVal = new UserRest();
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(userDetails, dto);

        UserDto created  = userService.updateUser(id,dto);
        BeanUtils.copyProperties(created, returnVal);

        return returnVal;
    }

    @DeleteMapping(path="/{id}")
    public OperationStatusModel deleteUser(@PathVariable String id) {
        OperationStatusModel model = new OperationStatusModel();
        model.setOperationName(RequestOperationName.DELETE.name());
        model.setOperationResult(RequestOperationStatus.SUCCESS.name());

        userService.deleteUser(id);

        return model;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value="page", defaultValue = "0") int page,
                                   @RequestParam(value="limit", defaultValue = "25") int limit) {
        List<UserRest> returnVal = new ArrayList<>();

        List<UserDto> userDtos = userService.getUsers(page, limit);

        for(UserDto dto : userDtos) {
            UserRest model = new UserRest();
            BeanUtils.copyProperties(dto, model);
            returnVal.add(model);
        }



        return returnVal;
    }
}
