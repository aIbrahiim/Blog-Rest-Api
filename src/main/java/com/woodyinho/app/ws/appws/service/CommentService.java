package com.woodyinho.app.ws.appws.service;

import com.woodyinho.app.ws.appws.share.dto.CommentDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
@Service
public interface CommentService {
    ResponseEntity<CommentDTO> add(CommentDTO commentDTO, String postId, Principal principal);

    List<CommentDTO> getAllCommentsByPostId(String postId);
}
