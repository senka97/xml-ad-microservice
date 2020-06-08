package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.UserCanPostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;

public interface UserCanPostCommentRepository extends JpaRepository<UserCanPostComment, Long> {

    @Query(value = "SELECT * FROM user_can_post_comment ucpc WHERE ucpc.car_id= ?1 and ucpc.user_id = ?2  and ucpc.posted = ?3 and ucpc.request_end_date <= ?4"  , nativeQuery = true)
    ArrayList<UserCanPostComment> findOut(Long adId, Long userId, Boolean alreadyPosted, LocalDate today);

}
