package com.woodyinho.app.ws.appws.io.repositories;

import com.woodyinho.app.ws.appws.io.entity.CommentEntity;
import com.woodyinho.app.ws.appws.io.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPost(PostEntity postEntity);
}
