package com.example.sns.user.model.response;

import com.example.sns.user.model.User;
import com.example.sns.user.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String userName;

    private UserRole role;

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getUserRole()
        );
    }
}
