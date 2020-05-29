package com.team19.admicroservice.dto;

import com.team19.admicroservice.model.Ad;

import java.time.LocalDate;

public class AdDTO {

    private Long id;

    private Long ownerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private int limitKm;

    private boolean cdw;

    private String location;

    private PriceListDTO priceList;

    private CarDTO car;

    public AdDTO(){

    }

    public AdDTO(Ad ad){
        this.id = ad.getId();
        this.ownerId = ad.getOwnerId();
        this.startDate = ad.getStartDate();
        this.endDate = ad.getEndDate();
        this.limitKm = ad.getLimitKm();
        this.cdw = ad.getCdw();
        this.location = ad.getLocation();
        this.priceList = new PriceListDTO(ad.getPriceList());
        this.car = new CarDTO();
        this.car.setId(ad.getCarId());
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

    public int getLimitKm() {
        return limitKm;
    }

    public void setLimitKm(int limitKm) {
        this.limitKm = limitKm;
    }

    public boolean isCdw() {
        return cdw;
    }

    public void setCdw(boolean cdw) {
        this.cdw = cdw;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PriceListDTO getPriceList() {
        return priceList;
    }

    public void setPriceList(PriceListDTO priceList) {
        this.priceList = priceList;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


}
