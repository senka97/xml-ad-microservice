package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AdRepository extends JpaRepository<Ad, Long> {

    ArrayList<Ad> findAllByCarId(Long id);
}
