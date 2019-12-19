package com.woodyinho.app.ws.appws.service.impl;

import com.woodyinho.app.ws.appws.exceptions.PostServiceException;
import com.woodyinho.app.ws.appws.exceptions.ResourceNotFoundException;
import com.woodyinho.app.ws.appws.exceptions.UserServiceException;
import com.woodyinho.app.ws.appws.io.entity.PostEntity;
import com.woodyinho.app.ws.appws.io.entity.UserEntity;
import com.woodyinho.app.ws.appws.io.repositories.PostRepository;
import com.woodyinho.app.ws.appws.io.repositories.UserRepository;
import com.woodyinho.app.ws.appws.model.PostRequestModel;
import com.woodyinho.app.ws.appws.response.ErrorMessages;
import com.woodyinho.app.ws.appws.response.PageRest;
import com.woodyinho.app.ws.appws.service.PostService;
import com.woodyinho.app.ws.appws.share.dto.PostDTO;
import com.woodyinho.app.ws.appws.shared.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    Utils utils;

    @Override
    public PageRest<PostDTO> getPosts(int page, int limit) {

        List<PostDTO> returnValue = new ArrayList<>();

        /*UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
*/
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "createDateTime");
        Page<PostEntity> postsPage = postRepository.findAll(pageable);

        if(postsPage.getNumberOfElements() == 0){
            return new PageRest<>(Collections.emptyList(), postsPage.getNumber(), postsPage.getSize(),
                    postsPage.getTotalElements(), postsPage.getTotalPages(), postsPage.isLast());
        }

        List<PostEntity> posts = postsPage.getContent();
        for(PostEntity postEntity : posts){
            returnValue.add(new ModelMapper().map(postEntity, PostDTO.class));
        }

        /*Iterable<PostEntity> postEntities = postRepository.findAllByUserDetails(userEntity);
        for(PostEntity postEntity : postEntities){
            returnValue.add(modelMapper.map(postEntity, PostDTO.class));
        }
*/
        return new PageRest<>(returnValue, postsPage.getNumber(), postsPage.getSize(), postsPage.getTotalElements(),
                postsPage.getTotalPages(), postsPage.isLast());
    }

    @Override
    public PostDTO getPost(String postId) {

        PostEntity postEntity = postRepository.findByPostId(postId)
                .orElseThrow(() ->  new ResourceNotFoundException("Post", "id", postId));

        PostDTO returnValue = new ModelMapper().map(postEntity, PostDTO.class);

       return returnValue;
    }

    @Override
    public PostDTO add(PostDTO postDTO, Principal principal) {

        PostEntity post = new ModelMapper().map(postDTO, PostEntity.class);

        UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));


        post.setUserDetails(userEntity);
        post.setPostId(utils.generatePostId(30));

         postRepository.save(post);
         postDTO = new ModelMapper().map(post, PostDTO.class);

         return postDTO;
    }

    @Override
    public boolean deletePost(String id, Principal principal) {
        String email = principal.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));

        PostEntity post = postRepository.findByPostId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        if(post.getUserDetails().getUserId() == user.getUserId()){
            postRepository.delete(post);
            return true;
        }

        return false;
    }

    @Override
    public PostDTO updatePost(String id, PostRequestModel post, Principal principal) {
        PostDTO returnValue = new PostDTO();

        PostEntity postEntity = postRepository.findByPostId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));


        UserEntity userEntity = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));

        if(postEntity.getUserDetails().getUserId() != userEntity.getUserId()){
            throw new ResourceNotFoundException("Post", "id", id);
        }

        postEntity.setTitle(post.getTitle());
        postEntity.setBody(post.getBody());

        PostEntity updatedPost = postRepository.save(postEntity);

        BeanUtils.copyProperties(updatedPost, returnValue);

        return returnValue;

    }




}
