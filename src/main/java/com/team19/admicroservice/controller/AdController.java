package com.team19.admicroservice.controller;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.AdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AdController {

    @Autowired
    private AdServiceImpl adService;

    @GetMapping(value="/ads", produces = "application/json")
    public ResponseEntity<?> getAllAds()  {

        return new ResponseEntity<>(adService.getAllAds(), HttpStatus.OK);
    }

    @GetMapping(value="/ads/{id}", produces = "application/json")
    public ResponseEntity<?> getAd(@PathVariable("id") Long id)  {

        return new ResponseEntity<>(adService.getAd(id), HttpStatus.OK);
    }

    @PostMapping(value = "/ad", consumes="application/json")
    public ResponseEntity<?> postAd(@RequestBody AdDTO adDTO){

        adService.postNewAd(adDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/ad/car/{car_id}/active", produces = "application/json")
    public ResponseEntity<?> checkIfCarHasActiveAds(@PathVariable("car_id") Long id)  {
        return new ResponseEntity<>(adService.carHasActiveAds(id), HttpStatus.OK);
    }
}
