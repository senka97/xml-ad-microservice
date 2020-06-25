package com.team19.admicroservice.controller;

import com.team19.admicroservice.service.impl.UserCanPostCommentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api")
public class UserCanPostCommentController {

    @Autowired
    private UserCanPostCommentServiceImpl userCanPostCommentService;

    Logger logger = LoggerFactory.getLogger(UserCanPostCommentController.class);

    @GetMapping(value= "/userCanPostComment/{adId}/{userId}")
    @PreAuthorize("hasAuthority('comment_create')")
    public ResponseEntity<?> canUserPostComment(@PathVariable("adId") Long adId, @PathVariable("userId") Long userId)
    {
       return  new ResponseEntity<>(this.userCanPostCommentService.canUserPostComment(adId, userId), HttpStatus.OK);
    }

    @PutMapping(value = "/userCanPostComment/{carId}/{userId}")
    @PreAuthorize("hasAuthority('comment_create')")
    public Boolean changeCanPostComment(@PathVariable("carId") Long carId, @PathVariable("userId") Long userId)
    {
        return this.userCanPostCommentService.changeCanPostComment(carId,userId);
    }

    // ovo ce se pozivati kada se korisniku odobri zahtev
    @PostMapping(value = "/userCanPostComment/{adId}/{uId}/{endDate}")
   // @PreAuthorize("hasAuthority('comment_create')") agent kada prihvata zahtev nece imati ovu permisiju, a tada se poziva ova funkcija
    public Boolean createUserCanPostComment(@PathVariable("adId") Long adId, @PathVariable("uId") Long userId, @PathVariable("endDate") String endDate)
    {
        try
        {
            LocalDate requestEndDate = LocalDate.parse(endDate);
            return this.userCanPostCommentService.createUserCanPostComment(adId,userId,requestEndDate);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error("Creating UCPC - Invalid date format");
            return false;
        }

    }
}
