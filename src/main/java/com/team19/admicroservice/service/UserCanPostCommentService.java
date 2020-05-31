package com.team19.admicroservice.service;

import com.team19.admicroservice.model.UserCanPostComment;

public interface UserCanPostCommentService {

    Boolean canUserPostComment(Long adId, Long userId);
    Boolean changeCanPostComment(Long adId, Long userId);
    UserCanPostComment createUserCanPostComment(Long adId, Long userId);
}
