package com.example.sns.post.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SnsException;
import com.example.sns.post.model.Post;
import com.example.sns.post.model.entity.LikeEntity;
import com.example.sns.post.model.entity.PostEntity;
import com.example.sns.post.repository.LikeRepository;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.user.model.entity.UserEntity;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final LikeRepository likeRepository;


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

    @Transactional
    public void delete(String userName, Long postId) {
        UserEntity userEntity = getUserEntityOrException(userName);
        PostEntity postEntity = getPostEntityOrException(postId);

        if (postEntity.getUser() != userEntity) {
            throw new SnsException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", userName, postId));
        }
        postRepository.delete(postEntity);
    }

    // user exist
    private UserEntity getUserEntityOrException(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new SnsException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
    }

    public Page<Post> list(Pageable pageable) {
        return postRepository.findAllByDeletedAtIsNull(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String userName, Pageable pageable) {
        UserEntity userEntity = getUserEntityOrException(userName);
        return postRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Long postId, String userName) {
        // post exist
        PostEntity postEntity = getPostEntityOrException(postId);
        UserEntity userEntity = getUserEntityOrException(userName);

        // check liked -> throw
        likeRepository.findByUserAndPostAndDeletedAtIsNull(userEntity, postEntity).ifPresent(it -> {
            throw new SnsException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
        });

        // like save
        likeRepository.save(LikeEntity.of(userEntity, postEntity));

    }

    public long likeCount(Long  postId) {
        PostEntity postEntity = getPostEntityOrException(postId);
        // count like
        return likeRepository.countByPostAndDeletedAtIsNull(postEntity);
    }
}
