package com.med.userapi.service;

import com.med.userapi.entity.dto.AuthenticationReq;
import com.med.userapi.entity.dto.AuthenticationRes;

public interface AuthenticationService {

    AuthenticationRes login(AuthenticationReq authenticationReq);
}
