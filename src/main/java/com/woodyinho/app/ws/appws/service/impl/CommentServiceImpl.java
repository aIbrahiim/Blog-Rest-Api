package com.woodyinho.app.ws.appws.service.impl;

import com.woodyinho.app.ws.appws.exceptions.PostServiceException;
import com.woodyinho.app.ws.appws.exceptions.UserServiceException;
import com.woodyinho.app.ws.appws.io.entity.CommentEntity;
import com.woodyinho.app.ws.appws.io.entity.PostEntity;
import com.woodyinho.app.ws.appws.io.entity.UserEntity;
import com.woodyinho.app.ws.appws.io.repositories.CommentRepository;
import com.woodyinho.app.ws.appws.io.repositories.PostRepository;
import com.woodyinho.app.ws.appws.io.repositories.UserRepository;
import com.woodyinho.app.ws.appws.response.ApiResponse;
import com.woodyinho.app.ws.appws.response.ErrorMessages;
import com.woodyinho.app.ws.appws.service.CommentService;
import com.woodyinho.app.ws.appws.share.dto.CommentDTO;
import com.woodyinho.app.ws.appws.shared.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    Utils utils;

    @Override
    public ResponseEntity<CommentDTO> add(CommentDTO commentDTO, String postId, Principal principal) {

        ModelMapper mapper = new ModelMapper();

        UserEntity user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        PostEntity post = postRepository.findByPostId(postId)
                .orElseThrow(() ->  new PostServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

            CommentEntity commentEntity = mapper.map(commentDTO, CommentEntity.class);
            commentEntity.setCommentId(utils.generateCommentId(30));
            commentEntity.setPost(post);
            commentEntity.setUserDetails(user);

            commentRepository.save(commentEntity);

            commentDTO = mapper.map(commentEntity, CommentDTO.class);

            return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);

    }

    @Override
    public List<CommentDTO> getAllCommentsByPostId(String postId) {

        PostEntity post = postRepository.findByPostId(postId)
                .orElseThrow(() ->  new PostServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        List<CommentEntity> comments = commentRepository.findAllByPost(post);
        List<CommentDTO> commentDTOS = new ArrayList<>();

        if (comments != null && !comments.isEmpty()) {
            java.lang.reflect.Type listType = new TypeToken<List<CommentDTO>>() {
            }.getType();
            commentDTOS = new ModelMapper().map(comments, listType);
        }
        return commentDTOS;
    }
}
