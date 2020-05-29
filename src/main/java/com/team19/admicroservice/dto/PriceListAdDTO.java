package com.team19.admicroservice.dto;


import com.team19.admicroservice.model.PriceList;

public class PriceListAdDTO {

    private Long adID;
    private double pricePerKm;
    private double pricePerDay;
    private int discount20Days;
    private int discount30Days;

    public PriceListAdDTO(){

    }

    public PriceListAdDTO(PriceList priceList){
        this.pricePerDay = priceList.getPricePerDay();
        this.pricePerKm = priceList.getPricePerKm();
        this.discount20Days = priceList.getDiscount20Days();
        this.discount30Days = priceList.getDiscount30Days();
    }

    public Long getAdID() {
        return adID;
    }

    public void setAdID(Long adID) {
        this.adID = adID;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public int getDiscount20Days() {
        return discount20Days;
    }

    public void setDiscount20Days(int discount20Days) {
        this.discount20Days = discount20Days;
    }

    public int getDiscount30Days() {
        return discount30Days;
    }

    public void setDiscount30Days(int discount30Days) {
        this.discount30Days = discount30Days;
    }
}
