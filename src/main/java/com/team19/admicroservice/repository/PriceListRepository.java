package com.team19.admicroservice.repository;

import com.team19.admicroservice.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PriceListRepository extends JpaRepository<PriceList, Long> {

    ArrayList<PriceList> findAllByOwnerIdAndRemoved(Long id, boolean removed);
    PriceList findByAliasAndOwnerIdAndRemoved(String alias, Long id, boolean removed);

}
