package com.javanauta.ts.taskscheduler.adapters.in.security;

import com.javanauta.ts.taskscheduler.adapters.out.client.user.dto.ExternalUserDTO;
import com.javanauta.ts.taskscheduler.ports.out.client.user.ExternalUserProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl {

    private final ExternalUserProvider externalUserProvider;

    public UserDetailsServiceImpl(ExternalUserProvider externalUserProvider) {
        this.externalUserProvider = externalUserProvider;
    }

    public UserDetails loadUserDetails(String email, String token) {
        ExternalUserDTO externalUserDTO = externalUserProvider.getUserByEmail(email, token);

        return org.springframework.security.core.userdetails.User
                .withUsername(externalUserDTO.getEmail()) // Sets the username as the email
                .password(externalUserDTO.getPassword()) // Sets the user's password
                .build(); // Builds the UserDetails object
    }
}
