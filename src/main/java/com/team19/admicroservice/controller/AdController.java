package com.team19.admicroservice.controller;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.dto.CartItemDTO;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.AdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value="/ads/{id}/owner", produces = "application/json")
    public Long getAdOwner(@PathVariable("id") Long id){
           return adService.getAdOwner(id);
    }

    @PostMapping(value="/ads/cartItems", consumes = "application/json", produces = "application/json")
    public List<CartItemDTO> findAds(@RequestBody List<CartItemDTO> cartItemDTOs){

         return adService.fillCartItems(cartItemDTOs);
    }

}
