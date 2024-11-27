package com.example.sns.post.repository;

import com.example.sns.post.model.entity.CommentEntity;
import com.example.sns.post.model.entity.PostEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPostAndDeletedAtIsNull(PostEntity post, Pageable pageable);

//    @Transactional
//    @Modifying
//    @Query("UPDATE CommentEntity entity SET deleted_at = NOW() where entity.post = :post")
//    void deleteAllByPost(@Param("post") PostEntity postEntity);

}
