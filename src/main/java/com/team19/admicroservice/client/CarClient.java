package com.team19.admicroservice.client;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "car-service")
public interface CarClient {

    @GetMapping("api/cars/{id}")
    CarDTO getCar(@PathVariable("id") Long id, @RequestHeader("permissions") String permissions,
                         @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token);

    @PostMapping("api/cars/findCars")
    ArrayList<AdDTO> findCars(@RequestBody List<AdDTO> ads, @RequestHeader("permissions") String permissions,
                             @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token );
}
