package com.example.sns.user.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.user.model.User;
import com.example.sns.user.model.entity.UserEntity;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public User join(String userName, String password) {
        // userName check
        userRepository.findByUserName(userName)
                .ifPresent(u -> {
                    throw new SnsException(ErrorCode.DUPLICATED_USER_NAME,
                            String.format("%s is duplicated.", userName)); }
                );

        // 회원가입 진행
        UserEntity userEntity = userRepository.save(UserEntity.fromUser(userName, passwordEncoder.encode(password)));
        return User.fromUserEntity(userEntity);
    }

    // TODO: jwt
    public String login(String userName, String password) {
        // 회원가입 여부 체크
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 체크
        if (userEntity.getPassword().equals(password)) {
            throw new SnsException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        return "";
    }
}
