package com.team19.admicroservice.service.impl;

import com.rent_a_car.ad_service.soap.AddPriceListRequest;
import com.rent_a_car.ad_service.soap.AddPriceListResponse;
import com.rent_a_car.ad_service.soap.DeletePriceListRequest;
import com.rent_a_car.ad_service.soap.DeletePriceListResponse;
import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.dto.PriceListRequestDTO;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.repository.AdRepository;
import com.team19.admicroservice.repository.PriceListRepository;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.PriceListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceListServiceImpl implements PriceListService {

    @Autowired
    private PriceListRepository priceListRepository;
    @Autowired
    private AdServiceImpl adService;

    Logger logger = LoggerFactory.getLogger(PriceListServiceImpl.class);

    @Override
    public ArrayList<PriceListDTO> getPriceListsFromUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        ArrayList<PriceList> priceLists = priceListRepository.findAllByOwnerIdAndRemoved(Long.parseLong(cp.getUserID()), false);
        ArrayList<PriceListDTO> priceListDTOS = new ArrayList<>();
        for(PriceList priceList: priceLists){
            PriceListDTO priceListDTO = new PriceListDTO(priceList);
            priceListDTOS.add(priceListDTO);
        }

        return priceListDTOS;
    }

    @Override
    public PriceList findById(Long id) {
        PriceList priceList = priceListRepository.findById(id).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        if(priceList==null){
            logger.warn(MessageFormat.format("PL:invalid-FBI NF;UserID:{0}", cp.getUserID()));//PL price list, FBI - find by id lol , NAF -Not found
        }else{
            logger.info(MessageFormat.format("PL-Id:{0}-FBI;UserID:{1}",priceList.getId(), cp.getUserID()));//PL price list, FBI - find by id lol
        }


        return priceList;
    }

    @Override
    public PriceList findByAliasForOwner(String alias) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        return this.priceListRepository.findByAliasAndOwnerIdAndRemoved(alias, Long.parseLong(cp.getUserID()), false);
    }

    @Override
    public List<PriceListAdDTO> findPriceLists(List<PriceListAdDTO> priceListAdDTOs) {

        for(PriceListAdDTO pl: priceListAdDTOs){
            Ad ad = adService.findById(pl.getAdID());
            if(ad != null){
                pl.setPricePerDay(ad.getPriceList().getPricePerDay());
                pl.setPricePerKm(ad.getPriceList().getPricePerKm());
                pl.setDiscount20Days(ad.getPriceList().getDiscount20Days());
                pl.setDiscount30Days(ad.getPriceList().getDiscount30Days());
                pl.setPriceForCdw(ad.getPriceList().getPriceForCdw());
                pl.setCdwAd(ad.getCdw());
            }
        }
        return priceListAdDTOs;
    }

    @Override
    public PriceList save(PriceList priceList) {
        return priceListRepository.save(priceList);
    }

    @Override
    public PriceListAdDTO getPriceListForAd(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        Ad ad = adService.findById(id);

        if(ad != null)
        {
            PriceListAdDTO pl = new PriceListAdDTO();
            pl.setPricePerDay(ad.getPriceList().getPricePerDay());
            pl.setPricePerKm(ad.getPriceList().getPricePerKm());
            pl.setDiscount20Days(ad.getPriceList().getDiscount20Days());
            pl.setDiscount30Days(ad.getPriceList().getDiscount30Days());
            pl.setAdID(ad.getId());
            pl.setPriceForCdw(ad.getPriceList().getPriceForCdw());
            pl.setCdwAd(ad.getCdw());
            logger.info(MessageFormat.format("PL-Id:{0}-RFA;UserID:{1}",ad.getPriceList().getId(), cp.getUserID()));//PL price list, RFA -Read From Ad

            return pl;
        }
        logger.warn(MessageFormat.format("PL-invalid:NAF-Id:{0};UserID:{1}",id, cp.getUserID()));//PL price list, NAF -No ad found
        return null;
    }

    @Override
    public PriceListDTO createNewPriceList(PriceListRequestDTO priceListRequestDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        PriceList priceList = new PriceList(priceListRequestDTO, Long.parseLong(cp.getUserID()));
        priceList = this.priceListRepository.save(priceList);
        PriceListDTO priceListDTO = new PriceListDTO(priceList);
        return priceListDTO;
    }

    @Override
    public boolean deletePriceList(Long id) {

         PriceList priceList = this.findById(id);
         List<Ad> activeAds = this.adService.findActiveAdsForThisPriceList(id, LocalDate.now());
         if(activeAds.size()>0){
             return false;
         }

         priceList.setRemoved(true);
         this.priceListRepository.save(priceList);
         return true;

    }

    @Override
    public AddPriceListResponse createNewPriceListFromAgentApp(AddPriceListRequest addPriceListRequest) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        PriceList exist = this.findByAliasForOwner(addPriceListRequest.getAlias());
        if(exist != null){
            AddPriceListResponse apResponse = new AddPriceListResponse();
            apResponse.setSuccess(false);
            apResponse.setMessage("You already have a price list with that alias.");
            apResponse.setMainId(0);
            return apResponse;
        }
        PriceList priceList = new PriceList(addPriceListRequest, Long.parseLong(cp.getUserID()));
        priceList = this.priceListRepository.save(priceList);
        AddPriceListResponse apResponse = new AddPriceListResponse();
        apResponse.setMainId(priceList.getId());
        apResponse.setSuccess(true);
        apResponse.setMessage("Success");

        return apResponse;
    }

    @Override
    public DeletePriceListResponse deletePriceListFromAgentApp(DeletePriceListRequest deletePriceListRequest) {

        PriceList priceList = this.findById(deletePriceListRequest.getMainId());
        if(priceList == null){
            DeletePriceListResponse dpResponse = new DeletePriceListResponse();
            dpResponse.setSuccess(false);
            dpResponse.setMessage("Price list with that id doesn't exist in the main app.");
            return dpResponse;
        }
        List<Ad> activeAds = this.adService.findActiveAdsForThisPriceList(deletePriceListRequest.getMainId(), LocalDate.now());
        if(activeAds.size()>0){
            DeletePriceListResponse dpResponse = new DeletePriceListResponse();
            dpResponse.setSuccess(false);
            dpResponse.setMessage("This price list can't be deleted because there are active ads which use it.");
            return dpResponse;
        }

        priceList.setRemoved(true);
        this.priceListRepository.save(priceList);
        DeletePriceListResponse dpResponse = new DeletePriceListResponse();
        dpResponse.setSuccess(true);
        dpResponse.setMessage("Success.");
        return dpResponse;

    }
}
