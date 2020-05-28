package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CartItemDTO;
import com.team19.admicroservice.dto.AdDTOSimple;

import java.util.ArrayList;
import java.util.List;

public interface AdService {

    ArrayList<AdDTO> getAllAds();
    AdDTO getAd(Long id);
    Long getAdOwner(Long id);
    List<CartItemDTO> fillCartItems(List<CartItemDTO> cartItemDTOs);
    AdDTOSimple getAdSimple(Long id);
}
