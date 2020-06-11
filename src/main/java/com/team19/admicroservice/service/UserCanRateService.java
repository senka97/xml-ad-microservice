package com.team19.admicroservice.service;

import java.time.LocalDate;

public interface UserCanRateService {

    boolean canRate(Long userId, Long adId);
    boolean changeCanRate(Long userId, Long carId);
    boolean createUserCanRate(Long userId, Long adId, LocalDate requestEndDate);
}
