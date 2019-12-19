package com.woodyinho.app.ws.appws.io.repositories;

import com.woodyinho.app.ws.appws.io.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Optional<PostEntity> findByPostId(String postId);
}
