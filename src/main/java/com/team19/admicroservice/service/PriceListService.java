package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.PriceList;

import java.util.ArrayList;

public interface PriceListService {

    ArrayList<PriceListDTO> getPriceListsFromUser();
    PriceList findById(Long id);
}
