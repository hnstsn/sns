package com.example.sns.post.model;


import com.example.sns.post.model.entity.PostEntity;
import com.example.sns.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {

    private Long id;

    private String title;

    private String body;

    private User user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;


    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                User.fromUserEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
