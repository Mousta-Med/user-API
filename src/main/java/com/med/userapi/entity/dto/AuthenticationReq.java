package com.med.userapi.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationReq {

    @NotEmpty(message = "username or email should be not empty")
    private String emailOrUsername;

    @NotEmpty(message = "password should be not empty")
    private String password;
}
