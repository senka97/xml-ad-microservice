package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.repository.AdRepository;
import com.team19.admicroservice.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;
}
