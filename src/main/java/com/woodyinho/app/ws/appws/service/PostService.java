package com.woodyinho.app.ws.appws.service;

import com.woodyinho.app.ws.appws.model.PostRequestModel;
import com.woodyinho.app.ws.appws.response.PageRest;
import com.woodyinho.app.ws.appws.share.dto.PostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Service
public interface PostService {

    PageRest<PostDTO> getPosts(int page, int limit);
    PostDTO getPost(String postId);
    PostDTO add(PostDTO postDTO, Principal principal);
    boolean deletePost(String id, Principal principal);
    PostDTO updatePost(String id, PostRequestModel post, Principal principal);
}
