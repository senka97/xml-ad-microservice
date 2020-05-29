package com.team19.admicroservice.dto;

import java.time.LocalDate;

public class CartItemDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long adID;
    private AdDTO ad;

    public CartItemDTO(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public AdDTO getAd() {
        return ad;
    }

    public void setAd(AdDTO adDTO) {
        this.ad = adDTO;
    }

    public Long getAdID() {
        return adID;
    }

    public void setAdID(Long adID) {
        this.adID = adID;
    }
}
