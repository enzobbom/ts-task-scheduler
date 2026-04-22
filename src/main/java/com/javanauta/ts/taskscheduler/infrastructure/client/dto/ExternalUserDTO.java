package com.javanauta.ts.taskscheduler.infrastructure.client.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExternalUserDTO {

    private String email;
    private String password;
}
