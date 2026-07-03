package com.javanauta.ts.taskscheduler.ports.out.client.user;

import com.javanauta.ts.taskscheduler.adapters.out.client.user.dto.ExternalUserDTO;

public interface ExternalUserProvider {
    ExternalUserDTO getUserByEmail(String email, String token);
}
