package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.repository.AdRepository;
import com.team19.admicroservice.repository.PriceListRepository;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceListServiceImpl implements PriceListService {

    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private AdRepository adRepository;

    @Override
    public ArrayList<PriceListDTO> getPriceListsFromUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        ArrayList<PriceList> priceLists = priceListRepository.findAllByOwnerId(Long.parseLong(cp.getUserID()));
        ArrayList<PriceListDTO> priceListDTOS = new ArrayList<>();
        for(PriceList priceList: priceLists){
            PriceListDTO priceListDTO = new PriceListDTO(priceList);
            priceListDTOS.add(priceListDTO);
        }

        return priceListDTOS;
    }

    @Override
    public PriceList findById(Long id) {
        return priceListRepository.findById(id).orElse(null);
    }

    @Override
    public List<PriceListAdDTO> findPriceLists(List<PriceListAdDTO> priceListAdDTOs) {

        for(PriceListAdDTO pl: priceListAdDTOs){
            Ad ad = adRepository.findById(pl.getAdID()).orElse(null);
            if(ad != null){
                pl.setPricePerDay(ad.getPriceList().getPricePerDay());
                pl.setPricePerKm(ad.getPriceList().getPricePerKm());
                pl.setDiscount20Days(ad.getPriceList().getDiscount20Days());
                pl.setDiscount30Days(ad.getPriceList().getDiscount30Days());
            }
        }
        return priceListAdDTOs;
    }
}
