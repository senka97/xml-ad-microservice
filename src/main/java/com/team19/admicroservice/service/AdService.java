package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.AdDTO;

import java.util.ArrayList;

public interface AdService {

    ArrayList<AdDTO> getAllAds();
    AdDTO getAd(Long id);
    AdDTO postNewAd(AdDTO adDTO);
    AdDTO carHasActiveAds(Long car_id);
}
