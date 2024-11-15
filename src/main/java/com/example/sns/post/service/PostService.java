package com.example.sns.post.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.post.model.entity.PostEntity;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.user.model.entity.UserEntity;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;


    @Transactional
    public void create(String title, String content, String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded.", userRepository)));

        postRepository.save(PostEntity.of(title, content, userEntity));
    }
}
