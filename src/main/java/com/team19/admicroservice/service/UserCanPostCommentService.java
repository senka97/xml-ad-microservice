package com.team19.admicroservice.service;

import com.team19.admicroservice.model.UserCanPostComment;

import java.time.LocalDate;

public interface UserCanPostCommentService {

    Boolean canUserPostComment(Long adId, Long userId);
    Boolean changeCanPostComment(Long carId, Long userId);
    Boolean createUserCanPostComment(Long adId, Long userId, LocalDate requestEndDate);
}
