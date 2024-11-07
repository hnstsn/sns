package com.example.sns.user.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.user.model.User;
import com.example.sns.user.model.entity.UserEntity;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    @Transactional
    public User join(String userName, String password) {
        // userName check
        userRepository.findByUserName(userName)
                .ifPresent(u -> {
                    throw new SnsException(ErrorCode.DUPLICATED_USER_NAME,
                            String.format("%s is duplicated.", userName)); }
                );

        // 회원가입 진행
        UserEntity userEntity = userRepository.save(UserEntity.fromUser(userName, password));
        return User.fromUserEntity(userEntity);
    }

}
