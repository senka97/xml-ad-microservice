package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.UserCanRate;
import com.team19.admicroservice.repository.UserCanRateRepository;
import com.team19.admicroservice.service.UserCanRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.MessageFormat;
import java.time.LocalDate;

@Service
public class UserCanRateServiceImpl implements UserCanRateService {

    @Autowired
    private UserCanRateRepository userCanRateRepository;

    @Autowired
    private AdServiceImpl adService;

    Logger logger = LoggerFactory.getLogger(UserCanRateServiceImpl.class);
    //UCR user can rate

    @Override
    public boolean canRate(Long userId, Long adId) {
        Ad ad = adService.findById(adId);

        if (ad != null) {
            LocalDate today = LocalDate.now();
            UserCanRate userCanRate = userCanRateRepository.findUserCanRate(ad.getCarId(), userId, false, today);

            if(userCanRate == null) {
                logger.warn(MessageFormat.format("UCR-not found;AdID:{0};UserID:{1}", adId, userId));
                return false;
            } else {
                logger.info(MessageFormat.format("UCR;AdID:{0};UserID:{1}", adId, userId));
                return true;
            }

        } else {
            logger.error(MessageFormat.format("UCR;AdID:{0}-not found;UserID:{1}", adId, userId));
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
            logger.info(MessageFormat.format("UCR-change;CarID:{0};UserID:{1}", carId, userId));
            return true;

        } else {
            logger.warn(MessageFormat.format("UCR-change-not found;CarID:{0};UserID:{1}", carId, userId));
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
            logger.info(MessageFormat.format("UCR-create;AdID:{0};For userID:{1}", adId, userId));
            return true;

        } else {
            logger.info(MessageFormat.format("UCR-create;AdID:{0}-not found;For userID:{1}", adId, userId));
            return false;
        }
    }
}
