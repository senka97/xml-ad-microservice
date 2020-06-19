package com.team19.admicroservice.model;

import com.rent_a_car.ad_service.soap.AddPriceListRequest;
import com.team19.admicroservice.dto.PriceListRequestDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ownerId")
    private Long ownerId;

    @Column(name="pricePerKm")
    private double pricePerKm;

    @Column(name="pricePerDay")
    private double pricePerDay;

    @Column(name="priceForCdw")
    private double priceForCdw;

    @Column(name="discount20Days")
    private int discount20Days;

    @Column(name="discount30Days")
    private int discount30Days;

    @Column(name="alias")
    private String alias;

    @Column(name="removed")
    private boolean removed;

    @OneToMany(mappedBy = "priceList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Ad> ads;

    public PriceList()
    {

    }

    public PriceList(PriceListRequestDTO plr, Long ownerId){
        this.alias = plr.getAlias();
        this.pricePerDay = plr.getPricePerDay();
        this.pricePerKm = plr.getPricePerKm();
        this.priceForCdw = plr.getPriceForCdw();
        this.discount20Days = plr.getDiscount20Days();
        this.discount30Days = plr.getDiscount30Days();
        this.ownerId = ownerId;
        this.removed = false;
        this.ads = new HashSet<>();
    }

    public PriceList(AddPriceListRequest apr, Long ownerId){
        this.alias = apr.getAlias();
        this.pricePerDay = apr.getPricePerDay();
        this.pricePerKm = apr.getPricePerKm();
        this.priceForCdw = apr.getPriceForCdw();
        this.discount20Days = apr.getDiscount20Days();
        this.discount30Days = apr.getDiscount30Days();
        this.ownerId = ownerId;
        this.removed = false;
        this.ads = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Set<Ad> getAds() {
        return ads;
    }

    public void setAds(Set<Ad> ads) {
        this.ads = ads;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public double getPriceForCdw() {
        return priceForCdw;
    }

    public void setPriceForCdw(double priceForCdw) {
        this.priceForCdw = priceForCdw;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
