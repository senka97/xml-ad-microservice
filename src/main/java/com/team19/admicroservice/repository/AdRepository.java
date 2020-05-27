package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
}
