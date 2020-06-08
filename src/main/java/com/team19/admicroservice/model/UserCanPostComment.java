package com.team19.admicroservice.model;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class UserCanPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="car_id")
    private Long carId;

    @Column(name="posted")
    private Boolean posted;

    @Column(name="request_end_date")
    private LocalDate requestEndDate;

    public UserCanPostComment()
    {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }

    public LocalDate getRequestEndDate() {
        return requestEndDate;
    }

    public void setRequestEndDate(LocalDate requestEndDate) {
        this.requestEndDate = requestEndDate;
    }
}
