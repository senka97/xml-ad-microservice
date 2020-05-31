package com.team19.admicroservice.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.team19.admicroservice.service.impl.UserCanPostCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class UserCanPostCommentController {

    @Autowired
    private UserCanPostCommentServiceImpl userCanPostCommentService;

    @GetMapping(value= "/canPost/{adId}/{userId}")
    @PreAuthorize("hasAuthority('comment_create')")
    public ResponseEntity<?> canUserPostComment(@PathVariable("adId") Long adId, @PathVariable("userId") Long userId)
    {
       return  new ResponseEntity<>(this.userCanPostCommentService.canUserPostComment(adId, userId), HttpStatus.OK);
    }

    @PutMapping(value = "/changeCanPostComment/{adId}/{userId}")
    @PreAuthorize("hasAuthority('comment_create')")
    public Boolean changeCanPostComment(@PathVariable("adId") Long adId, @PathVariable("userId") Long userId)
    {
        return this.userCanPostCommentService.changeCanPostComment(adId,userId);
    }

    // ovo ce se pozivati kada korisnik zavrsi sa rentiranjem kola
    @PostMapping(value = "/createCanPostComment/{adId}/{userId}")
    @PreAuthorize("hasAuthority('comment_create')")
    public ResponseEntity<?> createUserCanPostComment(@PathVariable("adId") Long adId, @PathVariable("userId") Long userId)
    {
        return new ResponseEntity<>(this.userCanPostCommentService.createUserCanPostComment(adId,userId), HttpStatus.CREATED);
    }
}
