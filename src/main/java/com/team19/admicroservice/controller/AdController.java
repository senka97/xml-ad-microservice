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

        if(adDTO.getCar().getCarModel() == null || adDTO.getCar().getCarClass() == null || adDTO.getCar().getFuelType() == null || adDTO.getCar().getTransType() == null){
            return new ResponseEntity<>("All information about car must be entered!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getStartDate() == null || adDTO.getEndDate() == null){
            return new ResponseEntity<>("Both start and end date must be selected",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getEndDate().isBefore(adDTO.getStartDate())){
            return new ResponseEntity<>("Start date must be before end date!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getPriceList().getId() == null){
            return new ResponseEntity<>("Price list must be selected!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getLimitKm() < 0){
            return new ResponseEntity<>("Kilometer limit cannot be negative!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getMileage() < 0){
            return new ResponseEntity<>("Mileage must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getChildrenSeats() < 0){
            return new ResponseEntity<>("Number of seats must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getChildrenSeats() > 4){
            return new ResponseEntity<>("Number of seats must be lower than 5",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getPhotos64().size() > 4){
            return new ResponseEntity<>("You cant ad more than 4 photos!",HttpStatus.BAD_REQUEST);
        }
        AdDTO newAd = adService.postNewAd(adDTO);

        //Ako servis vrati null znaci da ima vec maximum postavljenih oglasa
        if(newAd == null){
            return new ResponseEntity<>("You already have the limit of 3 active ads!",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newAd,HttpStatus.CREATED);
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
