package com.team19.admicroservice.controller;

import com.team19.admicroservice.client.UserClient;
import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.dto.PriceListRequestDTO;
import com.team19.admicroservice.dto.UserInfoDTO;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class PriceListController {

    @Autowired
    private PriceListServiceImpl priceListService;
    @Autowired
    private UserClient userClient;

    Logger logger = LoggerFactory.getLogger(PriceListController.class);

    @GetMapping(value = "/priceList/owner")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> getUsersPriceLists(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        logger.info("PL-ALL-read;UserID:" + cp.getUserID());
        return new ResponseEntity<>(priceListService.getPriceListsFromUser(), HttpStatus.OK);

    }

    @PostMapping(value = "/priceList")
    @PreAuthorize("hasAuthority('priceList_create')")
    ResponseEntity<?> createNewPriceList(@RequestBody PriceListRequestDTO priceListRequestDTO){

          Authentication auth = SecurityContextHolder.getContext().getAuthentication();
          CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

          if(priceListRequestDTO.getAlias() == null){
              logger.warn("PLR-invalid:alias M;UserID:" + cp.getUserID()); //PLR=price list request, M=missing
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alias for price list is mandatory.");
          }else{
              if(priceListRequestDTO.getAlias().equals("")){
                  logger.warn("PLR-invalid:alias M;UserID:" + cp.getUserID()); //PLR=price list request, M=missing
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alias for price list is mandatory.");
              }
          }
          if(priceListRequestDTO.getPricePerKm() <=0 || priceListRequestDTO.getPricePerDay()<=0 || priceListRequestDTO.getPriceForCdw()<=0){
              logger.warn("PLR-invalid:prices INV;UserID:" + cp.getUserID()); //PLR=price list request, INV=invalid
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prices have to be greater that 0.");
          }
          if(priceListRequestDTO.getDiscount30Days()<0 || priceListRequestDTO.getDiscount20Days()<0){
              logger.warn("PLR-invalid:discount INV;UserID:" + cp.getUserID()); //PLR=price list request, INV=invalid
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Discount has to be positive integer.");
          }else{
              UserInfoDTO userInfo = this.userClient.getUserInfo(Long.parseLong(cp.getUserID()), cp.getToken());
              if(userInfo.getRole().equals("ROLE_CLIENT") && (priceListRequestDTO.getDiscount30Days()>0 || priceListRequestDTO.getDiscount20Days()>0)){
                  logger.warn("PLR-invalid:discount INV;UserID:" + cp.getUserID()); //PLR=price list request, INV=invalid
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Clients can't define discount for their ads.");

              }
          }

          PriceList exist = this.priceListService.findByAliasForOwner(priceListRequestDTO.getAlias());

          if(exist != null){
              logger.info(MessageFormat.format("PLR-failed: alias {0} AE;UserID:{1}", priceListRequestDTO.getAlias(), cp.getUserID())); //PLR=price list request, AE=already exists
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have a price list with that alias.");
          }

          PriceListDTO priceListResponseDTO = this.priceListService.createNewPriceList(priceListRequestDTO);
        return new ResponseEntity(priceListResponseDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/priceList/{id}")
    @PreAuthorize("hasAuthority('priceList_delete')")
    public ResponseEntity<?> deletePriceList(@PathVariable("id") Long id){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();

        if(id <= 0){
            logger.warn(MessageFormat.format("PL-ID:{0} NV;UserID:{1}", id, cp.getUserID())); //NV=not valid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not valid. It has to be positive number.");
        }
        PriceList priceList = this.priceListService.findById(id);
        if(priceList == null){
            logger.warn(MessageFormat.format("PL-ID:{0} NF;UserID:{1}", id, cp.getUserID())); //NF=not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Price list with that id doesn't exist.");
        }else{
            if(priceList.isRemoved()){
                logger.warn(MessageFormat.format("PL-ID:{0} AR;UserID:{1}", id, cp.getUserID())); //AR=already removed
                return ResponseEntity.status(HttpStatus.GONE).body("Price list with that id doesn't exist anymore.");
            }
        }


        if(priceList.getOwnerId() != Long.parseLong(cp.getUserID())){
            logger.warn(MessageFormat.format("PL-ID:{0} DF-NUPL;UserID:{1}", id, cp.getUserID())); //DF=delete failed, NUPL=not users price list
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This is not your price list and you can't delete it.");
        }

        boolean success = this.priceListService.deletePriceList(id);
        if(success) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This price list can't be deleted because there are active ads which use it.");
        }
    }



    @PostMapping(value = "/priceList/ads")
    //@PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> findPriceLists(@RequestBody List<PriceListAdDTO> priceListAdDTOs){

          return new ResponseEntity(this.priceListService.findPriceLists(priceListAdDTOs), HttpStatus.OK);
    }

    @GetMapping(value = "/priceList/ad/{id}")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> getPriceListForAd(@PathVariable("id") Long adId){

        return new ResponseEntity<>(priceListService.getPriceListForAd(adId), HttpStatus.OK);
    }
}
