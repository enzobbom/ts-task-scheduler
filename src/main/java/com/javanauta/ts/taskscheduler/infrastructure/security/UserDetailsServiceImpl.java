package com.javanauta.ts.taskscheduler.infrastructure.security;

import com.javanauta.ts.taskscheduler.infrastructure.client.dto.ExternalUserDTO;
import com.javanauta.ts.taskscheduler.infrastructure.client.UserClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    private final UserClient userClient;

    public UserDetailsServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDetails loadUserDetails(String email, String token) {
        ExternalUserDTO externalUserDTO = userClient.getUserByEmail(email, token);

        return org.springframework.security.core.userdetails.User
                .withUsername(externalUserDTO.getEmail()) // Sets the username as the email
                .password(externalUserDTO.getPassword()) // Sets the user's password
                .build(); // Builds the UserDetails object
    }
}
