package com.med.userapi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationReq {

    private String email;
    private String password;
}
