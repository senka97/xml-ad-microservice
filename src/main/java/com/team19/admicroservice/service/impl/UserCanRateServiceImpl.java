package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.UserCanRate;
import com.team19.admicroservice.repository.UserCanRateRepository;
import com.team19.admicroservice.service.UserCanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserCanRateServiceImpl implements UserCanRateService {

    @Autowired
    private UserCanRateRepository userCanRateRepository;

    @Autowired
    private AdServiceImpl adService;

    @Override
    public boolean canRate(Long userId, Long adId) {
        Ad ad = adService.findById(adId);

        if (ad != null) {
            LocalDate today = LocalDate.now();
            UserCanRate userCanRate = userCanRateRepository.findUserCanRate(ad.getCarId(), userId, false, today);

            if(userCanRate == null) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    @Override
    public boolean changeCanRate(Long userId, Long carId) {
        LocalDate today = LocalDate.now();
        UserCanRate userCanRate = userCanRateRepository.findUserCanRate(carId, userId, false, today);

        if(userCanRate != null) {
            userCanRate.setRated(true);
            userCanRateRepository.save(userCanRate);
            return true;

        } else {
            return false;
        }
    }

    @Override
    public boolean createUserCanRate(Long userId, Long adId, LocalDate requestEndDate) {
        Ad ad = adService.findById(adId);

        if (ad != null) {
            UserCanRate userCanRate = new UserCanRate();
            userCanRate.setUserId(userId);
            userCanRate.setCarId(ad.getCarId());
            userCanRate.setRated(false);
            userCanRate.setRequestEndDate(requestEndDate);
            userCanRateRepository.save(userCanRate);
            return true;

        } else {
            return false;
        }
    }
}
