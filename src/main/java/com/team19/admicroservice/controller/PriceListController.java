package com.team19.admicroservice.controller;

import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class PriceListController {

    @Autowired
    private PriceListServiceImpl priceListService;

    @GetMapping(value = "/priceList/owner")
    ResponseEntity<?> getUsersPriceLists(){
        return new ResponseEntity<>(priceListService.getPriceListsFromUser(), HttpStatus.OK);

    }
}
