package com.example.sns.user;

import com.example.sns.user.model.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String userName, String password) {
        return UserEntity.of(userName, password);
    }
}
