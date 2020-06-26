package com.team19.admicroservice.soap;

import com.rent_a_car.ad_service.soap.*;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PriceListEndpoint {

    private static final String NAMESPACE_URI = "http://www.rent-a-car.com/ad-service/soap";

    @Autowired
    private PriceListServiceImpl priceListService;

    Logger logger = LoggerFactory.getLogger(PriceListEndpoint.class);

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addPriceListRequest")
    @ResponsePayload
    public AddPriceListResponse addPriceList(@RequestPayload AddPriceListRequest apr){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        logger.info("SR-add PL;UserID:" + cp.getUserID()); //SR=saop request

        String msg = "";
        boolean valid = true;
        if(apr.getAlias() == null){
            msg += "Alias for price list is mandatory. ";
            valid = false;
        }else{
            if(apr.getAlias().equals("")){
                msg += "Alias for price list is mandatory. ";
                valid = false;
            }
        }
        if(apr.getPricePerKm() <=0 || apr.getPricePerDay()<=0 || apr.getPriceForCdw()<=0){
            msg += "Prices have to be greater that 0. ";
            valid = false;
        }
        if(apr.getDiscount30Days()<0 || apr.getDiscount20Days()<0){
            msg += "Discount has to be positive integer.";
            valid = false;
        }
        if(!valid){
            logger.warn("SR-add PL failed:DNV;UserID:" + cp.getUserID()); //SR=saop request, DNV=data not valid
            AddPriceListResponse apResponse = new AddPriceListResponse();
            apResponse.setMessage(msg);
            apResponse.setSuccess(false);
            apResponse.setMainId(0);
            return apResponse;
        }

        AddPriceListResponse apResponse = this.priceListService.createNewPriceListFromAgentApp(apr);
        return apResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePriceListRequest")
    @ResponsePayload
    public DeletePriceListResponse deletePriceList(@RequestPayload DeletePriceListRequest dpr){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        logger.info("SR-delete PL;UserID:" + cp.getUserID()); //SR=saop request

        if(dpr.getMainId()<=0){
            logger.warn("SR-delete PL failed:IDNV;UserID:" + cp.getUserID()); //SR=saop request, IDNV=id not valid
            DeletePriceListResponse dpResponse = new DeletePriceListResponse();
            dpResponse.setMessage("Id is invalid. It has to be positive long number.");
            dpResponse.setSuccess(false);
        }

        DeletePriceListResponse dpResponse = this.priceListService.deletePriceListFromAgentApp(dpr);
        return dpResponse;
    }
}
