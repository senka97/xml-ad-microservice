package com.team19.admicroservice.controller;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CartItemDTO;
import com.team19.admicroservice.dto.AdDTOSimple;
import com.team19.admicroservice.service.impl.AdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @PostMapping(value = "/ad", consumes="application/json")
    public ResponseEntity<?> postAd(@RequestBody AdDTO adDTO){

        adService.postNewAd(adDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value="/ad/car/{car_id}/active", produces = "application/json")
    public ResponseEntity<?> checkIfCarHasActiveAds(@PathVariable("car_id") Long id)  {
        return new ResponseEntity<>(adService.carHasActiveAds(id), HttpStatus.OK);
    }


    @PostMapping(value="/ads/cartItems", consumes = "application/json", produces = "application/json")
    public List<CartItemDTO> findAds(@RequestBody List<CartItemDTO> cartItemDTOs){

         return adService.fillCartItems(cartItemDTOs);
    }


    @GetMapping(value="/getAd/{id}", produces = "application/json")
    public AdDTOSimple getAdSimple(@PathVariable("id") Long id)  {

        return adService.getAdSimple(id);
    }

    @PutMapping(value = "/ad/block/client/{id}")
    public ResponseEntity<?> hideAdsForBlockedClient(@PathVariable("id") Long id) {
        if(adService.hideAdsForBlockedClient(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/ad/activate/client/{id}")
    public ResponseEntity<?> showAdsForActiveClient(@PathVariable("id") Long id) {
        if(adService.showAdsForActiveClient(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
