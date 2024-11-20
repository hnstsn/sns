package com.example.sns.post.repository;

import com.example.sns.post.model.Post;
import com.example.sns.post.model.entity.PostEntity;
import com.example.sns.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.nio.channels.FileChannel;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);

    Page<PostEntity> findAllByDeletedAtIsNull(Pageable pageable);
}
