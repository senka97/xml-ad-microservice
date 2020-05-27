package com.team19.admicroservice.controller;

import com.team19.admicroservice.service.impl.AdServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AdController {

    @Autowired
    private AdServiceImpl adService;

}
