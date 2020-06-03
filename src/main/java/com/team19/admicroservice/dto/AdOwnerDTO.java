package com.team19.admicroservice.dto;

import java.time.LocalDate;

public class AdOwnerDTO {

    private Long ownerID;
    private LocalDate startDate;
    private LocalDate endDate;

    public AdOwnerDTO(){

    }

    public AdOwnerDTO(Long ownerID, LocalDate startDate, LocalDate endDate){
        this.ownerID = ownerID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Long ownerID) {
        this.ownerID = ownerID;
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
}
