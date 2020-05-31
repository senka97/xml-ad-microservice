package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.ArrayList;

public interface AdRepository extends JpaRepository<Ad, Long> {

    ArrayList<Ad> findAllByCarId(Long id);
    ArrayList<Ad> findAllByOwnerId(Long id);

    @Query("select a from Ad a where a.startDate <= ?1 and a.endDate >= ?2 and lower(a.location) like lower(?3)")
    ArrayList<Ad> simpleSerach(LocalDate fromDate, LocalDate toDate, String location);
}
