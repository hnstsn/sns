package com.example.sns.post.repository;

import com.example.sns.post.model.entity.LikeEntity;
import com.example.sns.post.model.entity.PostEntity;
import com.example.sns.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndPostAndDeletedAtIsNull(UserEntity user, PostEntity post);

    long countByPostAndDeletedAtIsNull(PostEntity post);

}
