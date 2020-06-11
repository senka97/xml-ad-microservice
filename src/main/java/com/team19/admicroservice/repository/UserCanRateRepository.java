package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.UserCanRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.ArrayList;

public interface UserCanRateRepository extends JpaRepository<UserCanRate, Long> {

    @Query(value = "SELECT * FROM user_can_rate ucr WHERE ucr.car_id= ?1 and ucr.user_id = ?2  and ucr.rated = ?3 and ucr.request_end_date <= ?4"  , nativeQuery = true)
    UserCanRate findUserCanRate(Long carId, Long userId, Boolean alreadyRated, LocalDate today);
}
