package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.repository.PriceListRepository;
import com.team19.admicroservice.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceListServiceImpl implements PriceListService {

    @Autowired
    private PriceListRepository priceListRepository;
}
