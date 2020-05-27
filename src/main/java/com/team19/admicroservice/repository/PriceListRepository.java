package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {
}
