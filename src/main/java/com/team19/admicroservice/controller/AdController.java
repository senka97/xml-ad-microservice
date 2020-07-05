package com.team19.admicroservice.controller;

import com.team19.admicroservice.client.CarClient;
import com.team19.admicroservice.dto.*;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.AdServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class AdController {

    @Autowired
    private AdServiceImpl adService;

    @Autowired
    private CarClient carClient;

    Logger logger = LoggerFactory.getLogger(AdController.class);

    @GetMapping(value="/ads", produces = "application/json")
    //posto ova metoda vise nece trebati, moze se dopuniti i iskoristiti kada se budu dobavljali oglasi za jednog korisnika npr
    public ResponseEntity<?> getAllAds()  {

        return new ResponseEntity<>(adService.getAllAds(), HttpStatus.OK);
    }

    @GetMapping(value="/ads/{id}", produces = "application/json")
    public ResponseEntity<?> getAd(@PathVariable("id") Long id)  {

            logger.info("get ad:" + id);
            return new ResponseEntity<>(adService.getAd(id), HttpStatus.OK);

    }

    @GetMapping(value="/ads/{id}/owner", produces = "application/json")
    public AdOwnerDTO getAdOwner(@PathVariable("id") Long id){
           return adService.getAdOwner(id);
    }


    @PreAuthorize("hasAuthority('ad_create')")
    @PostMapping(value = "/ad", consumes="application/json")
    public ResponseEntity<?> postAd(@RequestBody AdDTO adDTO){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();


        if(adDTO.getCar().getCarModel() == null || adDTO.getCar().getCarClass() == null || adDTO.getCar().getFuelType() == null || adDTO.getCar().getTransType() == null){
            logger.warn(MessageFormat.format("NAd-invalid:ad info missing;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("All information about car must be entered!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getStartDate() == null || adDTO.getEndDate() == null){
            logger.warn(MessageFormat.format("NAd-invalid:dates missing;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Both start and end date must be selected",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getEndDate().isBefore(adDTO.getStartDate())){
            logger.warn(MessageFormat.format("NAd-invalid:date order;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Start date must be before end date!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getPriceList().getId() == null){
            logger.warn(MessageFormat.format("NAd-invalid:price list missing;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Price list must be selected!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getLimitKm() < 0){
            logger.warn(MessageFormat.format("NAd-invalid:km limit less than 0;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Kilometer limit cannot be negative!",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getMileage() < 0){
            logger.warn(MessageFormat.format("NAd-invalid:mileage less than 0;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Mileage must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getChildrenSeats() < 0){
            logger.warn(MessageFormat.format("NAd-invalid:seats less than 0;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Number of seats must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getChildrenSeats() > 4){
            logger.warn(MessageFormat.format("NAd-invalid:seats greater than 4;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("Number of seats must be lower than 5",HttpStatus.BAD_REQUEST);
        }

        if(adDTO.getCar().getPhotos64().size() > 4){
            logger.warn(MessageFormat.format("NAd-invalid:photos greater than 4;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("You cant ad more than 4 photos!",HttpStatus.BAD_REQUEST);
        }
        AdDTO newAd = adService.postNewAd(adDTO);

        //Ako servis vrati null znaci da ima vec maximum postavljenih oglasa
        if(newAd == null){
            logger.warn(MessageFormat.format("NAd-invalid:max posted;UserID:{0}", cp.getUserID()));
            return new ResponseEntity<>("You already have the limit of 3 active ads!",HttpStatus.BAD_REQUEST);
        }

        logger.info(MessageFormat.format("Ad-ID:{0}-created;UserID:{1}", newAd.getId(), cp.getUserID()));
        return new ResponseEntity<>(newAd,HttpStatus.CREATED);
    }

    @GetMapping(value="/ad/car/{car_id}/active", produces = "application/json")
    public ResponseEntity<?> checkIfCarHasActiveAds(@PathVariable("car_id") Long id)  {
        return new ResponseEntity<>(adService.carHasActiveAds(id), HttpStatus.OK);
    }


    @PostMapping(value="/ads/cartItems", consumes = "application/json", produces = "application/json")
    public List<CartItemDTO> findAds(@RequestBody List<CartItemDTO> cartItemDTOs){

         return adService.fillCartItems(cartItemDTOs);
    }


    @GetMapping(value="/getAd/{id}", produces = "application/json")
    //treba svi da mogu da pristupe jer se poziva iz komentara, a njih svi mogu da citaju
    public AdDTOSimple getAdSimple(@PathVariable("id") Long id)  {

        return adService.getAdSimple(id);
    }

    @PutMapping(value = "/ad/client/{id}/blocked")
    public ResponseEntity<?> hideAdsForBlockedClient(@PathVariable("id") Long id) {
        adService.hideAdsForBlockedClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/ad/client/{id}/active")
    public ResponseEntity<?> showAdsForActiveClient(@PathVariable("id") Long id) {
        adService.showAdsForActiveClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(value="/ads/fill")
    public List<AdFrontDTO> fillAdsWithInformation(@RequestBody List<Long> adIDs){

          return this.adService.fillAdsWithInformation(adIDs);
    }

    @PutMapping(value="/ad/{id}/car")
    @PreAuthorize("hasAuthority('car_update')")
    public Boolean changeMileageAfterReport(@PathVariable("id") Long adId, @RequestBody double mileage)
    {
        return this.adService.changeMileageAfterReport(adId,mileage);
    }

    @GetMapping(value = "/ad/{id}/limitKm")
    @PreAuthorize("hasAuthority('report_create')")
    public Integer getLimitKm(@PathVariable("id") Long adId)
    {
        return this.adService.getLimitKm(adId);
    }

    @GetMapping(value="/ad/{from_date}/{to_date}/{location}", produces = "application/json")
    public ResponseEntity<?> simpleSearch(@PathVariable("from_date") String fromDateString,@PathVariable("to_date") String toDateString,@PathVariable("location") String location)  {
        System.out.println("Searching");
        LocalDate fromDate = LocalDate.parse(fromDateString);
        LocalDate toDate = LocalDate.parse(toDateString);
        LocalDate minDate = LocalDate.now();
        minDate = minDate.plusDays(2);



        if(fromDate == null || toDate == null){
            logger.warn("SSAd-invalid:date missing"); //SSAD- SIMPLE SEARCH AD
            return new ResponseEntity<>("Both pick-up and return date must be selected",HttpStatus.BAD_REQUEST);
        }

        if(fromDate.isBefore(minDate)){
            logger.warn("SSAd-invalid:minimal date"); //SSAD- SIMPLE SEARCH AD
            return new ResponseEntity<>("Pick-up date must be minimum 48 hours from today.",HttpStatus.BAD_REQUEST);
        }

        if(fromDate.isAfter(toDate)){
            logger.warn("SSAd-invalid:date order"); //SSAD- SIMPLE SEARCH AD
            return new ResponseEntity<>("Pick-up date cannot be after return date.",HttpStatus.BAD_REQUEST);
        }

        if(!location.matches("[a-zA-Z0-9 ]+$")){
            logger.warn("SSAd-invalid:location"); //SSAD- SIMPLE SEARCH AD
            return new ResponseEntity<>("Location cannot contain special characters!",HttpStatus.BAD_REQUEST);
        }

        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(location);

        logger.info("SSAd:read"); //SSAD- SIMPLE SEARCH AD
        return  new ResponseEntity<>(adService.simpleSerach(fromDate,toDate,location),HttpStatus.OK);
    }

    @GetMapping(value="/ad/{from_date}/{to_date}/{location}/{price_from}/{price_to}/{km_limit}/{cdw}", produces = "application/json")
    public ArrayList<AdDTO> extendedSearch(@PathVariable("from_date") String fromDateString, @PathVariable("to_date") String toDateString,
                                           @PathVariable("location") String location, @PathVariable("price_from") float priceFrom,
                                           @PathVariable("price_to") float priceTo, @PathVariable("km_limit") int kmLimit,
                                           @PathVariable("cdw") boolean cdw)  {


        System.out.println("Searching");
        LocalDate fromDate = LocalDate.parse(fromDateString);
        LocalDate toDate = LocalDate.parse(toDateString);
        System.out.println(fromDate);
        System.out.println(toDate);
        System.out.println(location);
        System.out.println(priceFrom);
        System.out.println(priceTo);
        System.out.println(kmLimit);
        System.out.println(cdw);

        return new ArrayList<>();// adService.extendedSearch(fromDate,toDate,location,Float.parseFloat(priceFrom),Float.parseFloat(priceTo),kmLimit,cdw);
    }


    //ova metoda bi trebalo ici u zuul odakle poziva ad client i car client i onda spaja ono sto je dobila
    //ali nisam uspeo da namestim to u zuulu jer mora da se stavi u header permisije token i id inace ga ne pusta da prodje (403
    //a posto tamo nema securitya ne mogu se izvuci te informacije
    //da je u zuulu radilo bi tako sto pola posla oko auta uradi car client isto pozivom kao i u ovoj metodi
    //a drugu polovinu ad tako sto bi se pozvala metoda extendedSearch()
    //i opet bi se spojili filterom kao i ovde
    @GetMapping("/search/ad/{from_date}/{to_date}/{location}/{price_from}/{price_to}/{km_limit}/{cdw}/car/{brand}/{model}/{feul_type}/{class_type}/{transmission_type}/{mileage}/{children_seats}")
    public ResponseEntity<?> search(@PathVariable("from_date") String fromDateString, @PathVariable("to_date") String toDateString,
                                    @PathVariable("location") String location, @PathVariable("price_from") float priceFrom,
                                    @PathVariable("price_to") float priceTo, @PathVariable("km_limit") int kmLimit,
                                    @PathVariable("cdw") boolean cdw,
                                    @PathVariable("brand") String brand,@PathVariable("model") String model,
                                    @PathVariable("feul_type") String feulType,@PathVariable("class_type") String classType,
                                    @PathVariable("transmission_type") String transType,@PathVariable("mileage") int mileage,
                                    @PathVariable("children_seats") int childrenSeats) {

        System.out.println("Searching");
        LocalDate fromDate = LocalDate.parse(fromDateString);
        LocalDate toDate = LocalDate.parse(toDateString);
        LocalDate minDate = LocalDate.now();
        minDate = minDate.plusDays(2);


        if(fromDate == null || toDate == null){
            logger.warn("ESAd-invalid:date missing"); //ESAd- EXTENDED SEARCH AD
            return new ResponseEntity<>("Both pick-up and return date must be selected",HttpStatus.BAD_REQUEST);
        }

        if(fromDate.isBefore(minDate)){
            logger.warn("ESAd-invalid:minimal date"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Pick-up date must be minimum 48 hours from today.",HttpStatus.BAD_REQUEST);
        }


        if(fromDate.isAfter(toDate)){
            logger.warn("ESAd-invalid:date order"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Pick-up date cannot be after return date.",HttpStatus.BAD_REQUEST);
        }

        if(kmLimit<0){
            logger.warn("ESAd-invalid:km limit less than 0"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Limit cannot be lower than 0.",HttpStatus.BAD_REQUEST);
        }

        if(mileage<0){
            logger.warn("ESAd-invalid:mileage less than 0"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Mileage cannot be lower than 0.",HttpStatus.BAD_REQUEST);
        }

        if(childrenSeats<0){
            logger.warn("ESAd-invalid:seats less than 0;UserID:{0}"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Number of seats for children cannot be lower than 0.",HttpStatus.BAD_REQUEST);
        }

        if(!location.matches("[a-zA-Z0-9 ]+$")){
            logger.warn("ESAd-invalid:location"); //ESAd- SIMPLE SEARCH AD
            return new ResponseEntity<>("Location cannot contain special characters!",HttpStatus.BAD_REQUEST);
        }

        ArrayList<AdDTO> adDTOS = adService.extendedSearch(fromDate,toDate,location,priceFrom,priceTo,kmLimit,cdw);

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        //ArrayList<CarDTO> cars = carClient.searchCars(brand,model,feulType,classType,transType,mileage,childrenSeats,cp.getPermissions(), cp.getUserID(), cp.getToken());
        ArrayList<CarDTO> cars = carClient.searchCars(brand,model,feulType,classType,transType,mileage,childrenSeats);

        System.out.println(adDTOS.size() + " "+cars.size());

        for (AdDTO adDTO : adDTOS) {
            adDTO.setCar(cars.stream().filter(o -> adDTO.getCar().getId().equals(o.getId())).findAny().orElse(null));
        }

        logger.info("ESAd:read"); //ESAd- Extended SEARCH AD
        return new ResponseEntity(adDTOS, HttpStatus.OK);
    }


}
