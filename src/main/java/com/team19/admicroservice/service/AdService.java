package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.AdDTOSimple;

import java.util.ArrayList;

public interface AdService {

    ArrayList<AdDTO> getAllAds();
    AdDTO getAd(Long id);
    AdDTOSimple getAdSimple(Long id);
}
