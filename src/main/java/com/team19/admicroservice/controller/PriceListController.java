package com.team19.admicroservice.controller;

import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class PriceListController {

    @Autowired
    private PriceListServiceImpl priceListService;

    @GetMapping(value = "/priceList/owner")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> getUsersPriceLists(){
        return new ResponseEntity<>(priceListService.getPriceListsFromUser(), HttpStatus.OK);

    }

    @PostMapping(value = "/priceList/ads")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> findPriceLists(@RequestBody List<PriceListAdDTO> priceListAdDTOs){

          return new ResponseEntity(this.priceListService.findPriceLists(priceListAdDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/priceList/ad/{id}")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> getPriceListForAd(@PathVariable("id") Long adId){

        return new ResponseEntity<>(priceListService.getPriceListForAd(adId), HttpStatus.OK);
    }
}
