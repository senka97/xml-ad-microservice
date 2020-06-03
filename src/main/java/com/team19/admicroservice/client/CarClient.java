package com.team19.admicroservice.client;

import com.team19.admicroservice.dto.AdDTO;
import com.team19.admicroservice.dto.CarDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "car-service")
public interface CarClient {

    @GetMapping(value = "api/cars/{id}", produces = "application/json")
    CarDTO getCar(@PathVariable("id") Long id);

    @PostMapping(value = "api/cars/findCars", consumes = "application/json", produces = "application/json")
    ArrayList<AdDTO> findCars(@RequestBody List<AdDTO> ads, @RequestHeader("permissions") String permissions,
                             @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token );

    @GetMapping("api/car")
    CarDTO addCar(@RequestBody CarDTO carDTO, @RequestHeader("permissions") String permissions,
                              @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token );

    @GetMapping(value="api/car/{brand}/{model}/{feul_type}/{class_type}/{transmission_type}/{mileage}/{children_seats}")
    ArrayList<CarDTO> searchCars(@PathVariable("brand") String brand,@PathVariable("model") String model,
                                 @PathVariable("feul_type") String feulType,@PathVariable("class_type") String classType,
                                 @PathVariable("transmission_type") String transType,@PathVariable("mileage") int mileage,
                                 @PathVariable("children_seats") int childrenSeats);
}
