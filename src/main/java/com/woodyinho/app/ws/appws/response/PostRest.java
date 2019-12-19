package com.woodyinho.app.ws.appws.response;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class PostRest extends RepresentationModel {

    private String postId;
    private String title;
    private String body;
    private List<CommentRest> comments;
    private LocalDateTime createDateTime;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public List<CommentRest> getComments() {
        return comments;
    }

    public void setComments(List<CommentRest> comments) {
        this.comments = comments;
    }
}
