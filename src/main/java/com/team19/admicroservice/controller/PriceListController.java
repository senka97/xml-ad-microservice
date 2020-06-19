package com.team19.admicroservice.controller;

import com.team19.admicroservice.dto.PriceListAdDTO;
import com.team19.admicroservice.dto.PriceListDTO;
import com.team19.admicroservice.dto.PriceListRequestDTO;
import com.team19.admicroservice.model.PriceList;
import com.team19.admicroservice.security.CustomPrincipal;
import com.team19.admicroservice.service.impl.PriceListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class PriceListController {

    @Autowired
    private PriceListServiceImpl priceListService;

    @GetMapping(value = "/priceList/owner")
    @PreAuthorize("hasAuthority('priceList_read')")
    ResponseEntity<?> getUsersPriceLists(){
        return new ResponseEntity<>(priceListService.getPriceListsFromUser(), HttpStatus.OK);

    }

    @PostMapping(value = "/priceList")
    @PreAuthorize("hasAuthority('priceList_create')")
    ResponseEntity<?> createNewPriceList(@RequestBody PriceListRequestDTO priceListRequestDTO){

          if(priceListRequestDTO.getAlias() == null){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alias for price list is mandatory.");
          }else{
              if(priceListRequestDTO.getAlias().equals("")){
                  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Alias for price list is mandatory.");
              }
          }
          if(priceListRequestDTO.getPricePerKm() <=0 || priceListRequestDTO.getPricePerDay()<=0 || priceListRequestDTO.getPriceForCdw()<=0){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Prices have to be greater that 0.");
          }
          if(priceListRequestDTO.getDiscount30Days()<0 || priceListRequestDTO.getDiscount20Days()<0){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Discount has to be positive integer.");
          }

          PriceList exist = this.priceListService.findByAliasForOwner(priceListRequestDTO.getAlias());

          if(exist != null){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already have a price list with that alias.");
          }

          PriceListDTO priceListResponseDTO = this.priceListService.createNewPriceList(priceListRequestDTO);
          return new ResponseEntity(priceListResponseDTO,HttpStatus.CREATED);
    }

    @DeleteMapping("/priceList/{id}")
    @PreAuthorize("hasAuthority('priceList_delete')")
    public ResponseEntity<?> deletePriceList(@PathVariable("id") Long id){

        if(id <= 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id is not valid. It has to be positive number.");
        }
        PriceList priceList = this.priceListService.findById(id);
        if(priceList == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Price list with that id doesn't exist.");
        }else{
            if(priceList.isRemoved()){
                return ResponseEntity.status(HttpStatus.GONE).body("Price list with that id doesn't exist anymore.");
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomPrincipal cp = (CustomPrincipal) auth.getPrincipal();
        if(priceList.getOwnerId() != Long.parseLong(cp.getUserID())){
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
