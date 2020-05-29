package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.AdDTOSimple;
import com.team19.admicroservice.dto.CarDTO;
import com.team19.admicroservice.dto.CartItemDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.Ad;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.repository.AdRepository;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.AdService;
import com.team19.admicroservice.service.PriceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;


@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private CarClient carClient;

    @Autowired
    private PriceListServiceImpl priceListService;
    @Override
    public ArrayList<AdDTO> getAllAds()
    {
        ArrayList<AdDTO> DTOAds = new ArrayList<>();

        List<Ad> ads = adRepository.findAll();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        for(Ad ad : ads)
        {

            AdDTO newAd = new AdDTO();
            CarDTO carDTO = new CarDTO();
            carDTO.setId(ad.getCarId());
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
            newAd.setOwnerId(ad.getOwnerId());
            newAd.setStartDate(ad.getStartDate());
            newAd.setEndDate(ad.getEndDate());
            newAd.setLimitKm(ad.getLimitKm());
            newAd.setCdw(ad.getCdw());
            newAd.setLocation(ad.getLocation());

            DTOAds.add(newAd);
        }
        //napravim listu AdDTO sa praznim CarDTO i saljem tu listu u car service, on tamo popuni info za kola i vrati mi listu adDTO
        ArrayList<AdDTO> DTOAds2 = this.carClient.findCars(DTOAds,cp.getPermissions(),cp.getUserID(),cp.getToken());

        return  DTOAds2;
    }

    @Override
    public AdDTO getAd(Long id){

        Ad ad = adRepository.findById(id).orElse(null);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        if(ad != null)
        {
            AdDTO newAd = new AdDTO();

            CarDTO carDTO = this.carClient.getCar(ad.getCarId(), cp.getPermissions(),cp.getUserID(),cp.getToken());
            System.out.println("Vratio se iz car service");
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
            newAd.setOwnerId(ad.getOwnerId());
            newAd.setStartDate(ad.getStartDate());
            newAd.setEndDate(ad.getEndDate());
            newAd.setLimitKm(ad.getLimitKm());
            newAd.setCdw(ad.getCdw());
            newAd.setLocation(ad.getLocation());

            return newAd;
        }
        else return null;

    }

    @Override
    public AdDTO postNewAd(AdDTO adDTO) {
        CarDTO car = new CarDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();


        System.out.println(adDTO.getLocation());
        System.out.println(adDTO.getCar().getCarBrand());
        if(adDTO.getCar().getId() == null){
            car = carClient.addCar(adDTO.getCar(), cp.getPermissions(),cp.getUserID(),cp.getToken());
        }else{
            car =carClient.getCar(adDTO.getCar().getId(), cp.getPermissions(),cp.getUserID(),cp.getToken());
        }

        Ad newAd = new Ad();
        newAd.setCarId(car.getId());
        newAd.setCdw(adDTO.isCdw());
        newAd.setStartDate(adDTO.getStartDate());
        newAd.setEndDate(adDTO.getEndDate());
        newAd.setLimitKm(adDTO.getLimitKm());
        newAd.setLocation(adDTO.getLocation());
        newAd.setOwnerId(Long.parseLong(cp.getUserID()));
        newAd.setPriceList(priceListService.findById(adDTO.getPriceList().getId()));
        adRepository.save(newAd);
        return adDTO;
    }

    //proverava da li auto ima aktivan oglas ako ima vraca id od tog oglasa ako ne vraca prazan objekat
    @Override
    public AdDTO carHasActiveAds(Long car_id) {
        System.out.println("*****Proveravam******");
        ArrayList<Ad> ads = adRepository.findAllByCarId(car_id);
        LocalDate now =LocalDate.now();
        AdDTO adDTO = new AdDTO();
        for(Ad ad: ads){
            if(ad.getEndDate().isAfter(now)){
                adDTO.setId(ad.getId());
                return adDTO;
            }
        }
        return null;
    }

    public Long getAdOwner(Long id) {

        Ad ad = adRepository.findById(id).orElse(null);
        if(ad == null) {
            return null;
        }else{
            return ad.getOwnerId();
        }
    }

    @Override
    public List<CartItemDTO> fillCartItems(List<CartItemDTO> cartItemDTOs) {
        List<AdDTO> adDTOs = new ArrayList<>();
        //prvo napravim dto od oglasa pa to prosledim u car-service gde se popuni podacima za car i onda to sve stavim u cartItem
        for(CartItemDTO ci: cartItemDTOs){
            if(!adDTOs.stream().filter(a -> a.getId() == ci.getAdID()).findFirst().isPresent()){ //proverim da li sam vec ubacila taj oglas
                Ad ad = adRepository.findById(ci.getAdID()).orElse(null);
                adDTOs.add(new AdDTO(ad));
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        System.out.println("Dodje ovde, broj oglasa: " + adDTOs.size());
        adDTOs = carClient.findCars(adDTOs, cp.getPermissions(), cp.getUserID(), cp.getToken());
        System.out.println("Izasao iz car-service");
        for(CartItemDTO ci: cartItemDTOs){
            AdDTO adDTO = adDTOs.stream().filter(a -> a.getId() == ci.getAdID()).findFirst().orElse(null);
            ci.setAd(adDTO);
        }
        return cartItemDTOs;
    }

    public AdDTOSimple getAdSimple(Long id) {

        AdDTOSimple adDTO = new AdDTOSimple();
        Ad ad = adRepository.findById(id).orElse(null);

        if(ad != null)
        {
            adDTO.setId(ad.getId());
            adDTO.setStartDate(ad.getStartDate());
            adDTO.setEndDate(ad.getEndDate());

            return adDTO;
        }

        else return null;
    }

}
