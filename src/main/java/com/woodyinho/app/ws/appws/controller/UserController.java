package com.woodyinho.app.ws.appws.controller;


import com.woodyinho.app.ws.appws.exceptions.UserServiceException;
import com.woodyinho.app.ws.appws.model.UserDetailsRequestModel;
import com.woodyinho.app.ws.appws.response.*;
import com.woodyinho.app.ws.appws.service.PostService;
import com.woodyinho.app.ws.appws.service.UserService;
import com.woodyinho.app.ws.appws.share.dto.PostDTO;
import com.woodyinho.app.ws.appws.share.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    PostService postsService;



    @GetMapping(
        path = "/profile",
        produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(Principal principal){
        return userService.getCurrentUser(principal);
    }




    @PutMapping(
            path = "/profile",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserRest updateUser(@RequestBody UserDetailsRequestModel newUser, Principal principal){

        UserRest returnValue = new UserRest();

        UserDTO userDTO = new ModelMapper().map(newUser, UserDTO.class);

        UserDTO updateUser = userService.updateUser(principal, userDTO);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;

    }

    @DeleteMapping( path = "/profile",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(Principal principal){

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(principal);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "3 ") int limit){

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDTO> users = userService.getUsers(page, limit);

        users.stream().forEach(userDTO -> {
            UserRest userRest = new UserRest();
            BeanUtils.copyProperties(userDTO, userRest);
            returnValue.add(userRest);
        });

        return returnValue;
    }


}
