package com.team19.admicroservice.service;

import com.team19.admicroservice.dto.*;
import com.team19.admicroservice.model.Ad;
import org.hibernate.mapping.Array;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface AdService {

    Ad findById(Long id);
    ArrayList<AdDTO> getAllAds();
    AdDTO getAd(Long id);
    AdDTO postNewAd(AdDTO adDTO);
    AdDTO carHasActiveAds(Long car_id);
    AdOwnerDTO getAdOwner(Long id);
    List<CartItemDTO> fillCartItems(List<CartItemDTO> cartItemDTOs);
    AdDTOSimple getAdSimple(Long id);
    ArrayList<Ad> getActiveAdsOfUser(Long id);
    void hideAdsForBlockedClient(Long id);
    void showAdsForActiveClient(Long id);
    List<AdFrontDTO> fillAdsWithInformation(List<Long> adIDs);
    ArrayList<AdDTO> simpleSerach(LocalDate fromDate,LocalDate toDate,String location);
    ArrayList<AdDTO> extendedSearch(LocalDate fromDate,LocalDate toDate,String location,float priceFrom,float priceTo,int kmLimit,boolean cdw);
    List<Ad> findActiveAdsForThisPriceList(Long id, LocalDate now);
}
