package com.example.sns.post.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostModifyRequest {

    private String title;

    private String body;
}
