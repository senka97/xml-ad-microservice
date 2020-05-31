package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.UserCanPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface UserCanPostCommentRepository extends JpaRepository<UserCanPostComment, Long> {

    @Query(value = "SELECT * FROM user_can_post_comment ucpc WHERE ucpc.ad_id= ?1 and ucpc.user_id = ?2  and ucpc.posted = ?3"  , nativeQuery = true)
    ArrayList<UserCanPostComment> findOut(Long adId, Long userId, Boolean alreadyPosted);

    @Query(value = "SELECT * FROM user_can_post_comment ucpc WHERE ucpc.ad_id= ?1 and ucpc.user_id = ?2  and ucpc.posted = ?3"  , nativeQuery = true)
    UserCanPostComment find(Long adId, Long userId, Boolean alreadyPosted);
}
