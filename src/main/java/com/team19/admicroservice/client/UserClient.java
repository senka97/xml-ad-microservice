package com.team19.admicroservice.client;

import com.team19.admicroservice.dto.UserDTO;
import com.team19.admicroservice.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping(value = "/user/currentUser", produces = "application/json")
    UserDTO user(@RequestHeader("permissions") String permissions,
                 @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token);

    @GetMapping(value = "client/{id}")
    boolean checkClientCanComment(@PathVariable Long id, @RequestHeader("permissions") String permissions,
                                  @RequestHeader("userID") String userId, @RequestHeader("Authorization") String token);

    @GetMapping("/user/info/{id}")
    UserInfoDTO getUserInfo(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

}
