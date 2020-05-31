package com.team19.admicroservice.dto;

public class CarFrontDTO {

    private Long id;
    private String photo64;
    private String carModel;
    private String carBrand;
    private double mileage;
    private int childrenSeats;
    private double rate;

    public CarFrontDTO(){

    }

    public CarFrontDTO(CarDTO carDTO){
        this.id = carDTO.getId();
        this.photo64 = carDTO.getPhotos64().get(0);
        this.carBrand = carDTO.getCarBrand();
        this.carModel = carDTO.getCarModel();
        this.mileage = carDTO.getMileage();
        this.childrenSeats = carDTO.getChildrenSeats();
        this.rate = carDTO.getRate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto64() {
        return photo64;
    }

    public void setPhoto64(String photo64) {
        this.photo64 = photo64;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public int getChildrenSeats() {
        return childrenSeats;
    }

    public void setChildrenSeats(int childrenSeats) {
        this.childrenSeats = childrenSeats;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
