package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.PriceList;

import java.util.ArrayList;
import java.util.List;

public interface PriceListService {

    ArrayList<PriceListDTO> getPriceListsFromUser();
    PriceList findById(Long id);
    List<PriceListAdDTO> findPriceLists(List<PriceListAdDTO> priceListAdDTOs);
    PriceList save(PriceList priceList);
    PriceListAdDTO getPriceListForAd(Long id);

}
