package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.model.UserCanPostComment;
import com.team19.admicroservice.repository.UserCanPostCommentRepository;
import com.team19.admicroservice.service.UserCanPostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;

@Service
public class UserCanPostCommentServiceImpl implements UserCanPostCommentService {

    @Autowired
    private UserCanPostCommentRepository userCanPostCommentRepository;

    @Override
    public Boolean canUserPostComment(Long adId, Long userId) {

        ArrayList<UserCanPostComment> list = this.userCanPostCommentRepository.findOut(adId, userId, false);

        if(list.isEmpty())
        {
            return false;
        }
        else return true;
    }

    @Override
    public Boolean changeCanPostComment(Long adId, Long userId) {

        ArrayList<UserCanPostComment> list = this.userCanPostCommentRepository.findOut(adId, userId, false);

        if(!list.isEmpty())
        {
            UserCanPostComment ucpc = list.get(0);
            ucpc.setPosted(true);
            this.userCanPostCommentRepository.save(ucpc);
            return true;
        }
        else return false;

    }

    @Override
    public UserCanPostComment createUserCanPostComment(Long adId, Long userId) {

        UserCanPostComment ucpc = new UserCanPostComment();

        ucpc.setAdId(adId);
        ucpc.setUserId(userId);
        ucpc.setPosted(false);

        return this.userCanPostCommentRepository.save(ucpc);
    }
}
