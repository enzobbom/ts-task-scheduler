package com.javanauta.ts.taskscheduler.presentation.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private String email;
    private String password;
}
