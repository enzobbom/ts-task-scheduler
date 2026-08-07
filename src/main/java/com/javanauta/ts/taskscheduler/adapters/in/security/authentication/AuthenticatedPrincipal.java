package com.javanauta.ts.taskscheduler.adapters.in.security.authentication;

import java.util.UUID;

public record AuthenticatedPrincipal(
        UUID id,
        String email
) {
}
