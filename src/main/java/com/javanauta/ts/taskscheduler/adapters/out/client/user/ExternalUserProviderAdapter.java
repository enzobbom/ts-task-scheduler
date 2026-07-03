package com.javanauta.ts.taskscheduler.adapters.out.client.user;

import com.javanauta.ts.taskscheduler.adapters.out.client.user.dto.ExternalUserDTO;
import com.javanauta.ts.taskscheduler.ports.out.client.user.ExternalUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExternalUserProviderAdapter implements ExternalUserProvider {
    private final FeignUserClient userProvider;

    @Override
    public ExternalUserDTO getUserByEmail(String email, String token) {
        return userProvider.getUserByEmail(email, token);
    }
}

