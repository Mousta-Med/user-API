package com.med.userapi.service.impl;

import com.med.userapi.entity.User;
import com.med.userapi.entity.dto.AuthenticationReq;
import com.med.userapi.entity.dto.AuthenticationRes;
import com.med.userapi.jwt.JWTUtil;
import com.med.userapi.repository.UserRepository;
import com.med.userapi.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationRes login(AuthenticationReq authenticationReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationReq.getEmail(),
                        authenticationReq.getPassword()
                )
        );
        String token = null;
        String email = authenticationReq.getEmail();
        Optional<User> user = userRepository.findByUsernameOrEmail(email, email);

        if (user.isPresent()) {
            token = jwtUtil.generateToken(user.get().getEmail());
            return new AuthenticationRes(token);
        }
        throw new UsernameNotFoundException("User Not Found With This Credential: " + authenticationReq.getEmail());
    }

}
