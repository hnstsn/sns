package com.example.sns.post.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.post.model.Post;
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

    @Transactional
    public Post modify(String title, String content, String userName, Long postId) {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded.", userRepository)));

        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);

        // post permission
        if (postEntity.getUser() != userEntity) {
            throw new SnsException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }

        postEntity.setTitle(title);
        postEntity.setContent(content);

        return Post.fromEntity(postRepository.saveAndFlush(postEntity));
    }

    // post exist
    private PostEntity getPostEntityOrException(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new SnsException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
    }

    // user exist
    private UserEntity getUserEntityOrException(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }
}
