package com.woodyinho.app.ws.appws.service.impl;

import com.woodyinho.app.ws.appws.exceptions.UserServiceException;
import com.woodyinho.app.ws.appws.io.repositories.PostRepository;
import com.woodyinho.app.ws.appws.io.repositories.UserRepository;
import com.woodyinho.app.ws.appws.io.entity.UserEntity;
import com.woodyinho.app.ws.appws.response.ErrorMessages;
import com.woodyinho.app.ws.appws.response.UserRest;
import com.woodyinho.app.ws.appws.service.UserService;
import com.woodyinho.app.ws.appws.share.dto.UserDTO;
import com.woodyinho.app.ws.appws.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    PostRepository postRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        if(!userRepository.findByEmail(userDTO.getEmail()).isEmpty())
            throw new RuntimeException("Email already used!");

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);


        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        UserEntity storedUserDetails = userRepository.save(userEntity);


        UserDTO returnValue = modelMapper.map(storedUserDetails, UserDTO.class);

        return returnValue;
    }

    @Override
    public UserDTO getUserByEmail(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        UserDTO returnValue = new UserDTO();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }



    @Override
    public UserDTO getUserByUserId(String userId) {

        UserDTO returnValue = new UserDTO();
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId));

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDTO updateUser(Principal principal, UserDTO user) {
        UserDTO returnValue = new UserDTO();
        UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));


        if(userEntity.getUserId() != user.getUserId())
            throw new UserServiceException(ErrorMessages.COULD_NOT_UPDATE_RECORD.getErrorMessage());

        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());

        UserEntity updatedUser = userRepository.save(userEntity);

        BeanUtils.copyProperties(updatedUser, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(Principal principal) {

        UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        userRepository.delete(userEntity);
    }



    @Override
    public List<UserDTO> getUsers(int page, int limit) {

        if(page > 0)
            page-=1;

        List<UserDTO> returnValue = new ArrayList<>();

        Pageable pageableRequest =  PageRequest.of(page, limit);
        Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = userPage.getContent();

        users.stream().forEach(userEntity -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
            returnValue.add(userDTO);
        });

        return returnValue;

    }

    @Override
    public UserRest getCurrentUser(Principal currentUser) {

        UserEntity userEntity = userRepository.findByEmail(currentUser.getName())
                .orElseThrow(() -> new UsernameNotFoundException(currentUser.getName()));

        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(userEntity, userRest);
        return userRest;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));


        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }



  /*  private UserDTO setAddressesId(UserDTO userDTO){

        for(int i=0; i<userDTO.getAddresses().size(); ++i){
            AddressDTO address = userDTO.getAddresses().get(i);
            address.setUserDetails(userDTO);
            address.setAddressId(utils.generateAddressId(30));
            userDTO.getAddresses().set(i, address);
        }
        return userDTO;
    }*/


}
