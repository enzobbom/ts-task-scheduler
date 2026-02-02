package com.javanauta.ts.taskscheduler.infrastructure.client;

import com.javanauta.ts.taskscheduler.business.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "${ts.user.service.url}")
public interface UserClient {

    @GetMapping("/user")
    UserDTO getUserByEmail(@RequestParam("email") String email, @RequestHeader("Authorization") String token);
}
