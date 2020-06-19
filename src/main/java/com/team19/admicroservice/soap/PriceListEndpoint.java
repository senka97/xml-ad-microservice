package com.team19.admicroservice.soap;

import com.rent_a_car.ad_service.soap.AddPriceListRequest;
import com.rent_a_car.ad_service.soap.AddPriceListResponse;
import com.rent_a_car.ad_service.soap.DeletePriceListRequest;
import com.rent_a_car.ad_service.soap.DeletePriceListResponse;
import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PriceListEndpoint {

    private static final String NAMESPACE_URI = "http://www.rent-a-car.com/ad-service/soap";

    @Autowired
    private PriceListServiceImpl priceListService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addPriceListRequest")
    @ResponsePayload
    public AddPriceListResponse addPriceList(@RequestPayload AddPriceListRequest apr){

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

        if(dpr.getMainId()<=0){
            DeletePriceListResponse dpResponse = new DeletePriceListResponse();
            dpResponse.setMessage("Id is invalid. It has to be positive long number.");
            dpResponse.setSuccess(false);
        }

        DeletePriceListResponse dpResponse = this.priceListService.deletePriceListFromAgentApp(dpr);
        return dpResponse;
    }
}
