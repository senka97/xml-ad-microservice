package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CarDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.repository.AdRepository;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;

    private CarClient carClient;

    @Override
    public ArrayList<AdDTO> getAllAds()
    {
        ArrayList<AdDTO> DTOAds = new ArrayList<>();

        System.out.println("Usao u getAds Servis");
        List<Ad> ads = adRepository.findAll();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        for(Ad ad : ads)
        {

            AdDTO newAd = new AdDTO();

            CarDTO carDTO = new CarDTO();
            //CarDTO carDTO = this.carClient.getCar(ad.getCarId(), cp.getPermissions(),cp.getUserID(),cp.getToken());
            //System.out.println("Vratio se iz car service");
            newAd.setCar(carDTO);

            PriceList priceList = ad.getPriceList();
            PriceListDTO priceListDTO = new PriceListDTO();

            priceListDTO.setId(priceList.getId());
            priceListDTO.setAlias(priceList.getAlias());
            priceListDTO.setPricePerDay(priceList.getPricePerDay());
            priceListDTO.setPricePerKm(priceList.getPricePerKm());
            priceListDTO.setDiscount20Days(priceList.getDiscount20Days());
            priceListDTO.setDiscount30Days(priceList.getDiscount30Days());
            newAd.setPriceList(priceListDTO);

            newAd.setId(ad.getId());
            newAd.setStartDate(ad.getStartDate());
            newAd.setEndDate(ad.getEndDate());
            newAd.setLimitKm(ad.getLimitKm());
            newAd.setCdw(ad.getCdw());
            newAd.setLocation(ad.getLocation());

            DTOAds.add(newAd);
        }

        return DTOAds;
    }

    @Override
    public AdDTO getAd(Long id){

        Ad ad = adRepository.findById(id).orElse(null);

        if(ad != null)
        {
            AdDTO newAd = new AdDTO();

            CarDTO carDTO = new CarDTO();
            //CarDTO carDTO = this.carClient.getCar(ad.getCarId(), cp.getPermissions(),cp.getUserID(),cp.getToken());
            //System.out.println("Vratio se iz car service");
            newAd.setCar(carDTO);

            PriceList priceList = ad.getPriceList();
            PriceListDTO priceListDTO = new PriceListDTO();

            priceListDTO.setId(priceList.getId());
            priceListDTO.setAlias(priceList.getAlias());
            priceListDTO.setPricePerDay(priceList.getPricePerDay());
            priceListDTO.setPricePerKm(priceList.getPricePerKm());
            priceListDTO.setDiscount20Days(priceList.getDiscount20Days());
            priceListDTO.setDiscount30Days(priceList.getDiscount30Days());
            newAd.setPriceList(priceListDTO);

            newAd.setId(ad.getId());
            newAd.setStartDate(ad.getStartDate());
            newAd.setEndDate(ad.getEndDate());
            newAd.setLimitKm(ad.getLimitKm());
            newAd.setCdw(ad.getCdw());
            newAd.setLocation(ad.getLocation());

            return newAd;
        }
        else return null;

    }
}
