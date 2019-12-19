package com.woodyinho.app.ws.appws.controller;

import com.woodyinho.app.ws.appws.io.entity.CommentEntity;
import com.woodyinho.app.ws.appws.model.CommentRequestModel;
import com.woodyinho.app.ws.appws.response.CommentRest;
import com.woodyinho.app.ws.appws.service.CommentService;
import com.woodyinho.app.ws.appws.share.dto.CommentDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @GetMapping
    public List<CommentRest> getAllComments(@PathVariable String postId){
        List<CommentDTO> commentDTOS = commentService.getAllCommentsByPostId(postId);

        List<CommentRest> commentRests = new ArrayList<>();

        if (commentDTOS != null && !commentDTOS.isEmpty()) {
            java.lang.reflect.Type listType = new TypeToken<List<CommentRest>>() {
            }.getType();
            commentRests = new ModelMapper().map(commentDTOS, listType);
        }
        return commentRests;
    }

    @PostMapping
    public ResponseEntity<CommentRest> addComment(@RequestBody CommentRequestModel commentRequestModel,
                                  @PathVariable String postId, Principal principal){


        ModelMapper mapper = new ModelMapper();

        CommentDTO commentDTO = mapper.map(commentRequestModel, CommentDTO.class);

        ResponseEntity<CommentDTO> commentResEntity =  commentService.add(commentDTO, postId, principal);
        CommentRest returnValue = mapper.map(commentResEntity.getBody(), CommentRest.class);

        return new ResponseEntity<>(returnValue, HttpStatus.CREATED);

    }
}
