package com.woodyinho.app.ws.appws.service;

import com.woodyinho.app.ws.appws.response.UserRest;
import com.woodyinho.app.ws.appws.share.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
@Service
public interface UserService extends UserDetailsService{
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);

    UserDTO getUserByUserId(String userId);
    UserDTO updateUser(Principal principal, UserDTO user);
    void deleteUser(Principal userId);
    List<UserDTO> getUsers(int page, int limit);
    UserRest getCurrentUser(Principal currentUser);
}
