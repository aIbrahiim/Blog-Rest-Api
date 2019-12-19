package com.woodyinho.app.ws.appws.controller;

import com.woodyinho.app.ws.appws.model.PostRequestModel;
import com.woodyinho.app.ws.appws.response.*;
import com.woodyinho.app.ws.appws.service.PostService;
import com.woodyinho.app.ws.appws.share.dto.PostDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public PageRest<PostRest> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "30 ") int limit
            ){

        List<PostRest> postsList = new ArrayList<>();

        PageRest<PostDTO> postDTOS = postService.getPosts(page, limit);

        if(postDTOS.getSize() != 0){
            java.lang.reflect.Type listType = new TypeToken<List<PostRest>>() {}.getType();
            postsList = new ModelMapper().map(postDTOS.getContent(), listType);

            for(PostRest postRest : postsList){
                Link postLink = linkTo(methodOn(PostController.class).getPost(postRest.getPostId())).withSelfRel();
                postRest.add(postLink);

            }
        }

        return new PageRest<>(postsList, postDTOS.getPage(), postDTOS.getSize(), postDTOS.getTotalElements(),
                postDTOS.getTotalPages(), postDTOS.isLast());
    }

    @PostMapping
    public ResponseEntity<PostRest> addPost(@RequestBody PostRequestModel post, Principal principal){

        ModelMapper mapper = new ModelMapper();

        PostDTO postDTO  = mapper.map(post, PostDTO.class);
        postDTO = postService.add(postDTO, principal);

        PostRest returnValue = mapper.map(postDTO, PostRest.class);

        return new ResponseEntity<>(returnValue, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostRest> getPost(@PathVariable String id){

        PostDTO postDTO = postService.getPost(id);

        Link postLink = linkTo(methodOn(PostController.class).getPost(id)).withSelfRel();
        Link postsLink = linkTo(methodOn(PostController.class).getAllPosts(0, 30)).withRel("posts");

        PostRest returnValue = new ModelMapper().map(postDTO, PostRest.class);
        returnValue.add(postLink);
        returnValue.add(postsLink);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable String id, Principal principal){

        boolean result = postService.deletePost(id, principal);

        if(result)
            return new ResponseEntity<>(new ApiResponse(true, "You've successfully deleted the post"), HttpStatus.OK);

        else
            return new ResponseEntity<>(new ApiResponse(true, "You don't have permission to delete this post"), HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PostRest> updatePost(@PathVariable String id,
                               @RequestBody PostRequestModel post, Principal principal){

        ModelMapper mapper = new ModelMapper();
        PostDTO postDTO = mapper.map(post, PostDTO.class);

        PostDTO updatedPost = postService.updatePost(id, post, principal);

        PostRest returnValue = mapper.map(updatedPost, PostRest.class);

        return new ResponseEntity<>(returnValue, HttpStatus.OK);

    }

}
