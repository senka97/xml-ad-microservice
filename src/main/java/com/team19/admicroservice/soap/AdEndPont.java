package com.team19.admicroservice.soap;

import com.rent_a_car.ad_service.soap.*;
import com.rent_a_car.ad_service.soap.PostAdResponse;
import com.team19.admicroservice.client.UserClient;
import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CarDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.AdServiceImpl;

import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;

@Endpoint
public class AdEndPont {
    private static final String NAMESPACE_URI = "http://www.rent-a-car.com/ad-service/soap";

    @Autowired
    private AdServiceImpl adService;

    @Autowired
    private UserClient userClient;

    @Autowired
    private PriceListServiceImpl priceListService;

    Logger logger = LoggerFactory.getLogger(AdEndPont.class);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postAdRequest")
    @ResponsePayload
    @PreAuthorize("hasAuthority('ad_create')")
    public PostAdResponse getTest(@RequestPayload PostAdRequest request) {
        System.out.println("Soap request");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        LocalDate startDate = LocalDate.parse(request.getStartDate());

        LocalDate endDate = LocalDate.parse(request.getEndDate());

        PostAdResponse response = new PostAdResponse();


        if(request.getCar().getCarModel() == null || request.getCar().getCarClass() == null || request.getCar().getFeulType() == null || request.getCar().getTransType() == null){
            logger.warn(MessageFormat.format("SRAd-invalid:ad info missing;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
            //return new ResponseEntity<>("All information about car must be entered!", HttpStatus.BAD_REQUEST);
        }

        if(request.getStartDate() == null || request.getEndDate() == null){
            logger.warn(MessageFormat.format("SRAd-invalid:dates missing;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
            //return new ResponseEntity<>("Both start and end date must be selected",HttpStatus.BAD_REQUEST);
        }

        if(endDate.isBefore(startDate)){
            logger.warn(MessageFormat.format("SRAd-invalid:date order;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
            //return new ResponseEntity<>("Start date must be before end date!",HttpStatus.BAD_REQUEST);
        }

        if(request.getLimitKm() < 0){
            logger.warn(MessageFormat.format("SRAd-invalid:km limit less than 0;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
           // return new ResponseEntity<>("Kilometer limit cannot be negative!",HttpStatus.BAD_REQUEST);
        }

        if(request.getCar().getMileage() < 0){
            logger.warn(MessageFormat.format("SRAd-invalid:mileage less than 0;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
           //return new ResponseEntity<>("Mileage must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(request.getCar().getChildrenSeats() < 0){
            logger.warn(MessageFormat.format("SRAd-invalid:seats less than 0;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
            //return new ResponseEntity<>("Number of seats must be greater or equal to 0",HttpStatus.BAD_REQUEST);
        }

        if(request.getCar().getChildrenSeats() > 4){
            logger.warn(MessageFormat.format("SRAd-invalid:seats greater than 4;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
            //return new ResponseEntity<>("Number of seats must be lower than 5",HttpStatus.BAD_REQUEST);
        }

        if(request.getCar().getPhotos64().size() > 4){
            logger.warn(MessageFormat.format("SRAd-invalid:photos greater than 4;UserID:{0}", cp.getUserID()));
            response.setIdAd(0);
            return response;
           // return new ResponseEntity<>("You cant ad more than 4 photos!",HttpStatus.BAD_REQUEST);
        }






        System.out.println("0");
        AdDTO newAdDTO = new AdDTO();

        newAdDTO.setOwnerId(request.getOwnerId());
        newAdDTO.setStartDate(startDate);
        newAdDTO.setEndDate(endDate);
        newAdDTO.setCdw(request.isCdw());
        newAdDTO.setLimitKm(request.getLimitKm());
        newAdDTO.setLocation(request.getLocation());
        newAdDTO.setLimitKm(request.getLimitKm());
        System.out.println("1");
        //ako je == 0 onda znaci da taj price list ne postoji pa cemo ga dodati
        //ovo je samo ako se recimo novi agent zakaci pa ima vec odredjen broj price listova kod sebe koji nisu dodati na glavnom
        if(request.getPriceList().getId() != 0) {
            System.out.println("Postoji price");
            PriceListDTO priceListDTO = new PriceListDTO();
            priceListDTO.setId(request.getPriceList().getId());
            newAdDTO.setPriceList(priceListDTO); //postavi id od pricelista samo je to vazno jer je price list vec dodat

        }else{
            //dodaj price list
            System.out.println("Novi price");
            com.team19.admicroservice.model.PriceList priceList = new PriceList();
            priceList.setAlias(request.getPriceList().getAlias());
            priceList.setDiscount20Days(request.getPriceList().getDiscount20Days());
            priceList.setDiscount30Days(request.getPriceList().getDiscount30Days());
            priceList.setPricePerDay(request.getPriceList().getPricePerDay());
            priceList.setPricePerKm(request.getPriceList().getPricePerKm());
            priceList.setOwnerId(Long.parseLong(cp.getUserID()));
            priceList.setPriceForCdw(request.getPriceList().getPriceForCdw());
            priceList = priceListService.save(priceList);

            PriceListDTO priceListDTO = new PriceListDTO();
            priceListDTO.setId(priceList.getId());





            newAdDTO.setPriceList(priceListDTO); //postavi id od pricelista samo je to vazno jer je price list vec dodat

        }
        System.out.println("2");
        CarDTO carDTO = new CarDTO();

        if(request.getCar().getId() !=0)
            carDTO.setId(request.getCar().getId());
        System.out.println("3");
        carDTO.setCarModel(request.getCar().getCarModel().replace("_"," "));
        carDTO.setCarBrand(request.getCar().getCarBrand().replace("_"," "));
        carDTO.setCarClass(request.getCar().getCarClass().replace("_"," "));
        carDTO.setChildrenSeats(request.getCar().getChildrenSeats());
        carDTO.setFuelType(request.getCar().getFeulType().replace("_"," "));
        carDTO.setHasAndroidApp(request.getCar().isHasAndroidApp());
        carDTO.setMileage(request.getCar().getMileage());
        carDTO.setTransType(request.getCar().getTransType().replace("_"," "));
        carDTO.setPhotos64(new ArrayList<>());
        carDTO.setRate(request.getCar().getRate());
        for(String img: request.getCar().getPhotos64()){
            System.out.println("Slika: "+img);
            carDTO.getPhotos64().add(img);
        }

        newAdDTO.setCar(carDTO);
        System.out.println("Postavlja request");
        AdDTO postedAd = adService.postNewAd(newAdDTO);

        response.setIdAd(postedAd.getId());
        response.setIdCar(postedAd.getCar().getId());
        response.setIdPriceList(postedAd.getPriceList().getId());
        response.setToken(newAdDTO.getCar().getAndroidToken());
        logger.info("SR-add ad;UserID:" + cp.getUserID()); //SR=saop request

        System.out.println("zavrsio request");
        return response;

    }

}
