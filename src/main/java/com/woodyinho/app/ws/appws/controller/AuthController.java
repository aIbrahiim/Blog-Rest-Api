package com.woodyinho.app.ws.appws.controller;

import com.woodyinho.app.ws.appws.model.UserDetailsRequestModel;
import com.woodyinho.app.ws.appws.response.UserRest;
import com.woodyinho.app.ws.appws.service.UserService;
import com.woodyinho.app.ws.appws.share.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {


    @Autowired
    UserService userService;

    @PostMapping(path = "/signup")
    public ResponseEntity<UserRest> createUser(@RequestBody UserDetailsRequestModel userDetailsRequestModel) throws Exception{
        UserRest returnValue = new UserRest();


        ModelMapper modelMapper = new ModelMapper();
        UserDTO userDTO = modelMapper.map(userDetailsRequestModel, UserDTO.class);
        UserDTO createdUser = userService.createUser(userDTO);
        returnValue = modelMapper.map(createdUser, UserRest.class);

        return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
    }
}
