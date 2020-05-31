package com.team19.admicroservice.model;


import javax.persistence.*;

@Entity
public class UserCanPostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="ad_id")
    private Long adId;

    @Column(name="posted")
    private Boolean posted;

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

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public Boolean getPosted() {
        return posted;
    }

    public void setPosted(Boolean posted) {
        this.posted = posted;
    }
}
