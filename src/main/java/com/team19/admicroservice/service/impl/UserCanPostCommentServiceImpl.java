package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.client.UserClient;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.UserCanPostComment;
import com.team19.admicroservice.repository.UserCanPostCommentRepository;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.UserCanPostCommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class UserCanPostCommentServiceImpl implements UserCanPostCommentService {

    @Autowired
    private UserCanPostCommentRepository userCanPostCommentRepository;

    @Autowired
    private AdServiceImpl adService;

    @Autowired
    private UserClient userClient;

    Logger logger = LoggerFactory.getLogger(UserCanPostCommentServiceImpl.class);
    //UCPC - UserCanPostComment

    @Override
    public Boolean canUserPostComment(Long adId, Long userId) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        Ad ad = adService.findById(adId);

        if(userClient.checkClientCanComment(userId, cp.getPermissions(), cp.getUserID(), cp.getToken()))
        {
            if (ad != null)
            {
                LocalDate today = LocalDate.now();
                ArrayList<UserCanPostComment> list = this.userCanPostCommentRepository.findOut(ad.getCarId(), userId, false, today);
                // moci ce da okaci komentar samo ako ga nije vec okacio i ako je iznajmljivanje zavrseno (endDate zahteva manji od danas)
                if (list.isEmpty())
                {
                    logger.info("UCPC - UserId: " + userId + " can't post comment adId: "+ adId);
                    return false;
                }
                else
                {
                    logger.info("UCPC - UserId: " + userId + " can post comment adId: " + adId);
                    return true;
                }
            }
            else
            {
                logger.error("UCPC - AdId: " + adId + " not found");
                return false;
            }
        }
        else
        {
            logger.info("UCPC - User id: " + userId + " blocked for posting comments");
            return false;
        }
    }

    @Override
    public Boolean changeCanPostComment(Long carId, Long userId) {

        LocalDate today = LocalDate.now();
        ArrayList<UserCanPostComment> list = this.userCanPostCommentRepository.findOut(carId, userId, false, today);

        if(!list.isEmpty())
        {
            UserCanPostComment ucpc = list.get(0);
            ucpc.setPosted(true);
            this.userCanPostCommentRepository.save(ucpc);
            logger.info("Changing UCPC - UserId: " + userId + " posted comment on carId: " + carId);
            return true;
        }
        else
        {
            logger.warn("Changing UCPC - UserId: " + userId + " couldn't comment on carId: " + carId);
            return false;
        }

    }

    @Override
    public Boolean createUserCanPostComment(Long adId, Long userId, LocalDate requestEndDate) {

        Ad ad = adService.findById(adId);

        if(ad != null)
        {
            UserCanPostComment ucpc = new UserCanPostComment();

            ucpc.setCarId(ad.getCarId());
            ucpc.setUserId(userId);
            ucpc.setRequestEndDate(requestEndDate);
            ucpc.setPosted(false);

            this.userCanPostCommentRepository.save(ucpc);
            logger.info("Creating UCPC - Created for userId: " + userId + " and adId: " + adId);
            return true;
        }
        else
        {
            logger.error("Creating UCPC - AdId: " + adId + " not found");
            return false;
        }

    }
}
