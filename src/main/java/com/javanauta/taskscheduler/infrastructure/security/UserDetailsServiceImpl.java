package com.javanauta.taskscheduler.infrastructure.security;

import com.javanauta.taskscheduler.business.dto.UserDTO;
import com.javanauta.taskscheduler.infrastructure.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl {

    @Autowired
    private UserClient userClient;

    public UserDetails loadUserDetails(String email, String token) {
        UserDTO userDTO = userClient.getUserByEmail(email, token);

        return org.springframework.security.core.userdetails.User
                .withUsername(userDTO.getEmail()) // Sets the username as the email
                .password(userDTO.getPassword()) // Sets the user's password
                .build(); // Builds the UserDetails object
    }
}
