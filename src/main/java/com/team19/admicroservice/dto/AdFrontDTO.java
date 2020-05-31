package com.team19.admicroservice.dto;

import java.time.LocalDate;

public class AdFrontDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private boolean cdw;
    private int limitKm;
    private CarFrontDTO car;

    public AdFrontDTO(){

    }

    public AdFrontDTO(AdDTO adDTO){
        this.id = adDTO.getId();
        this.startDate = adDTO.getStartDate();
        this.endDate = adDTO.getEndDate();
        this.location = adDTO.getLocation();
        this.cdw = adDTO.isCdw();
        this.limitKm = adDTO.getLimitKm();
        this.car = new CarFrontDTO(adDTO.getCar());
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isCdw() {
        return cdw;
    }

    public void setCdw(boolean cdw) {
        this.cdw = cdw;
    }

    public int getLimitKm() {
        return limitKm;
    }

    public void setLimitKm(int limitKm) {
        this.limitKm = limitKm;
    }

    public CarFrontDTO getCar() {
        return car;
    }

    public void setCar(CarFrontDTO car) {
        this.car = car;
    }
}
