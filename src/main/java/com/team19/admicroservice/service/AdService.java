package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CartItemDTO;
import com.team19.admicroservice.dto.AdDTOSimple;
import com.team19.admicroservice.model.Ad;

import java.util.ArrayList;
import java.util.List;

public interface AdService {

    ArrayList<AdDTO> getAllAds();
    AdDTO getAd(Long id);
    AdDTO postNewAd(AdDTO adDTO);
    AdDTO carHasActiveAds(Long car_id);
    Long getAdOwner(Long id);
    List<CartItemDTO> fillCartItems(List<CartItemDTO> cartItemDTOs);
    AdDTOSimple getAdSimple(Long id);
    boolean hideAdsForBlockedClient(Long id);
    boolean showAdsForActiveClient(Long id);
}
