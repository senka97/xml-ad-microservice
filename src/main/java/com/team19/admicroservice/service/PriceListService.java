package com.team19.admicroservice.service;

import com.rent_a_car.ad_service.soap.AddPriceListRequest;
import com.rent_a_car.ad_service.soap.AddPriceListResponse;
import com.rent_a_car.ad_service.soap.DeletePriceListRequest;
import com.rent_a_car.ad_service.soap.DeletePriceListResponse;
import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.dto.PriceListRequestDTO;
import com.team19.admicroservice.model.PriceList;

import java.util.ArrayList;
import java.util.List;

public interface PriceListService {

    ArrayList<PriceListDTO> getPriceListsFromUser();
    PriceList findById(Long id);
    PriceList findByAliasForOwner(String alias);
    List<PriceListAdDTO> findPriceLists(List<PriceListAdDTO> priceListAdDTOs);
    PriceList save(PriceList priceList);
    PriceListAdDTO getPriceListForAd(Long id);
    PriceListDTO createNewPriceList(PriceListRequestDTO priceListRequestDTO);
    boolean deletePriceList(Long id);
    AddPriceListResponse createNewPriceListFromAgentApp(AddPriceListRequest addPriceListRequest);
    DeletePriceListResponse deletePriceListFromAgentApp(DeletePriceListRequest deletePriceListRequest);

}
