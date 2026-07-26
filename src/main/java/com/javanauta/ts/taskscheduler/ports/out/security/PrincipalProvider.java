package com.javanauta.ts.taskscheduler.ports.out.security;

import java.util.UUID;

public interface PrincipalProvider {
    UUID getId();
    String getEmail();
}