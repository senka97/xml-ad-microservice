package com.team19.admicroservice.controller;

import com.team19.admicroservice.service.impl.UserCanRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/rating")
public class UserCanRateController {

    @Autowired
    private UserCanRateServiceImpl userCanRateService;

    @GetMapping(value= "/user/{userId}/ad/{adId}")
    @PreAuthorize("hasAuthority('rate_create')")
    public ResponseEntity<?> canRate(@PathVariable("userId") Long userId, @PathVariable("adId") Long adId) {
        return new ResponseEntity<>(userCanRateService.canRate(userId, adId), HttpStatus.OK);
    }

    @PostMapping(value = "/user/{userId}/ad/{adId}/{endDate}")
    public boolean createUserCanRate(@PathVariable("userId") Long userId, @PathVariable("adId") Long adId, @PathVariable("endDate") String endDate) {
        LocalDate requestEndDate = LocalDate.parse(endDate);
        return userCanRateService.createUserCanRate(userId, adId, requestEndDate);
    }

    @PutMapping(value = "/user/{userId}/car/{carId}")
    @PreAuthorize("hasAuthority('rate_update')")
    public boolean changeCanRate(@PathVariable("userId") Long userId, @PathVariable("carId") Long carId) {
        return userCanRateService.changeCanRate(userId, carId);
    }
}
