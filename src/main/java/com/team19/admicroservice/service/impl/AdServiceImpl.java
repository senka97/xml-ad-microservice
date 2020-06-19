package com.team19.admicroservice.service.impl;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.client.UserClient;
import com.team19.admicroservice.dto.*;
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

import java.lang.reflect.Array;
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

    @Autowired
    private UserClient userClient;


    @Override
    public Ad findById(Long id) {
        return adRepository.findById(id).orElse(null);
    }

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
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        if(ad != null)
        {
            AdDTO newAd = new AdDTO();

            CarDTO carDTO = this.carClient.getCar(ad.getCarId());
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
        System.out.println("posting1");
        CarDTO car = new CarDTO();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        System.out.println("posting2"+cp.getUserID());
        //Uzimam usera iz user servica da vidim da li je client ako jeste proveravam koliko ima aktivnih oglasa
        //Ako ima vise od 3 ne moze da postavi oglas
        if (userClient.user(cp.getPermissions(), cp.getUserID(), cp.getToken()).getRole().equals("ROLE_CLIENT")) {
            System.out.println(getActiveAdsOfUser(Long.parseLong(cp.getUserID())).size());
            if (getActiveAdsOfUser(Long.parseLong(cp.getUserID())).size() >= 3) {
                return null;
            }
            adDTO.setOwnerId(Long.parseLong(cp.getUserID()));
        }

        System.out.println("posting3");

        if (adDTO.getCar().getId() == null) {
            //ako je izabran novi auto salje zahtev na car client za kreiranje novog auta
             auth = SecurityContextHolder.getContext().getAuthentication();
             cp = (CustomPrincipal) auth.getPrincipal();
             car = carClient.addCar(adDTO.getCar(), cp.getPermissions(), cp.getUserID(), cp.getToken());
        } else {
            //ako je izabran postojeci auto koji nema aktivan oglas onda se salje get zahtev car clijentu za taj auto
            System.out.println("posting4");

            car = carClient.getCar(adDTO.getCar().getId());
        }
        System.out.println("posting5");
        Ad newAd = new Ad();
        newAd.setCarId(car.getId());
        newAd.setCdw(adDTO.isCdw());
        newAd.setStartDate(adDTO.getStartDate());
        newAd.setEndDate(adDTO.getEndDate());
        newAd.setLimitKm(adDTO.getLimitKm());
        newAd.setLocation(adDTO.getLocation());
        newAd.setOwnerId(Long.parseLong(cp.getUserID()));
        System.out.println("posting6");
        PriceList priceList = priceListService.findById(adDTO.getPriceList().getId());
        System.out.println(priceList.getId());
        newAd.setPriceList(priceList);
        newAd.setVisible(true);
        System.out.println("posting");
        newAd=adRepository.save(newAd);

        adDTO.setId(newAd.getId());
        CarDTO carDTO = new CarDTO();
        carDTO.setId(newAd.getCarId());
        PriceListDTO plDTO = new PriceListDTO();
        plDTO.setId(newAd.getPriceList().getId());
        adDTO.setCar(carDTO);
        adDTO.setPriceList(plDTO);

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

    public AdOwnerDTO getAdOwner(Long id) {

        Ad ad = adRepository.findById(id).orElse(null);
        if(ad == null) {
            return null;
        }else{
            return new AdOwnerDTO(ad.getOwnerId(),ad.getStartDate(),ad.getEndDate());
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
            adDTO.setCarId(ad.getCarId());

            return adDTO;
        }

        else return null;
    }

    @Override
    public ArrayList<Ad> getActiveAdsOfUser(Long id) {
        ArrayList<Ad> ads = adRepository.findAllByOwnerId(id);
        ArrayList<Ad> activeAds = new ArrayList<>();

        for(Ad ad: ads){
            if(ad.getEndDate().isAfter(LocalDate.now())){
                activeAds.add(ad);
            }
        }
        return activeAds;
    }
  
    @Override
    public void hideAdsForBlockedClient(Long id) {
        ArrayList<Ad> ads = adRepository.findAllByOwnerId(id);

        if (!ads.isEmpty()) {
            for (Ad a : ads) {
                a.setVisible(false);
                adRepository.save(a);
            }
        }
    }

    @Override
    public void showAdsForActiveClient(Long id) {
        ArrayList<Ad> ads = adRepository.findAllByOwnerId(id);

        if (!ads.isEmpty()) {
            for (Ad a : ads) {
                a.setVisible(true);
                adRepository.save(a);
            }
        }
    }

    @Override
    public List<AdFrontDTO> fillAdsWithInformation(List<Long> adIDs) {

        List<AdDTO> adDTOs = new ArrayList<>();
        List<AdFrontDTO> adFrontDTOs = new ArrayList<>();
        for (Long id : adIDs) {
            Ad ad = adRepository.findById(id).orElse(null);
            if (ad != null) {
                adDTOs.add(new AdDTO(ad));
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        adDTOs = this.carClient.findCars(adDTOs, cp.getPermissions(), cp.getUserID(), cp.getToken());
        for (AdDTO adDTO : adDTOs) {
            adFrontDTOs.add(new AdFrontDTO(adDTO));
        }

        return adFrontDTOs;
    }

    public ArrayList<AdDTO> simpleSerach(LocalDate fromDate, LocalDate toDate, String location) {
        ArrayList<Ad> ads = adRepository.simpleSerach(fromDate,toDate,location);
        ArrayList<AdDTO> adDTOS = new ArrayList<>();
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        for(Ad ad:ads){
            System.out.println("Visible: "+ad.isVisible());
            if(ad.isVisible()) {
                AdDTO adDTO = new AdDTO(ad);
                //CarDTO carDTO = this.carClient.getCar(ad.getCarId(), cp.getPermissions(), cp.getUserID(), cp.getToken());
                CarDTO carDTO = this.carClient.getCar(ad.getCarId());
                adDTO.setCar(carDTO);
                adDTOS.add(adDTO);
            }
        }

        return adDTOS;
    }

    @Override
    public ArrayList<AdDTO> extendedSearch(LocalDate fromDate, LocalDate toDate, String location, float priceFrom, float priceTo, int kmLimit, boolean cdw) {
        ArrayList<Ad> ads = adRepository.simpleSerach(fromDate,toDate,location);
        ArrayList<AdDTO> adDTOS = new ArrayList<>();
        for(Ad ad:ads) {
            System.out.println("Visible: " + ad.isVisible());
            if (ad.isVisible()) {

                if(priceTo != 0) {
                    if (ad.getPriceList().getPricePerDay() > priceTo) {
                        System.out.println("PRICE MIN");
                        continue;
                    }
                }

                if(priceFrom != 0) {
                    if (ad.getPriceList().getPricePerDay() < priceFrom) {
                        System.out.println("PRICE MAX");
                        continue;
                    }
                }

                if(kmLimit != 0) {
                    if (ad.getLimitKm() < kmLimit) {
                        System.out.println("KM LIM");
                        continue;
                    }
                }

                if(ad.getCdw()!= cdw) {
                    System.out.println("CDW");
                    continue;
                }

                AdDTO adDTO = new AdDTO(ad);
                adDTO.setCar(new CarDTO());
                adDTO.getCar().setId(ad.getCarId());
                adDTOS.add(adDTO);
            }
        }
        return adDTOS;
    }

    @Override
    public List<Ad> findActiveAdsForThisPriceList(Long id, LocalDate now) {

        return this.adRepository.findActiveAdsWithThisPriceList(id, LocalDate.now());
    }

    @Override
    public Boolean changeMileageAfterReport(Long adId, double mileage) {

        Ad ad = adRepository.findById(adId).orElse(null);

        // TODO proveriti da li je oglas unlimited ili ne, pa ako je prekoracio kilometre da se dodatno naplati

        if(ad != null)
        {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

            if(this.carClient.changeCarMileageAfterReport(ad.getCarId(), mileage, cp.getPermissions(), cp.getUserID(), cp.getToken()))
            {
                return true;
            }
            else return false;
        }
        else return false;

    }
}
