package com.javanauta.ts.taskscheduler.adapters.out.client.user;

import com.javanauta.ts.taskscheduler.adapters.out.client.user.dto.ExternalUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "${ts.user.service.uri}")
public interface FeignUserClient {

    @GetMapping
    ExternalUserDTO getUserByEmail(@RequestParam("email") String email, @RequestHeader("Authorization") String token);
}
