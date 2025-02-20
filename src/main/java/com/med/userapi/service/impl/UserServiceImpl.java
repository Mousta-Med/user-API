package com.med.userapi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import com.med.userapi.entity.User;
import com.med.userapi.enums.Role;
import com.med.userapi.jwt.JWTUtil;
import com.med.userapi.repository.UserRepository;
import com.med.userapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public List<User> generateUsers(int count) {
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setBirthDate(LocalDate.of(faker.number().numberBetween(1970, 2005), faker.number().numberBetween(1, 12), faker.number().numberBetween(1, 28)));
            user.setCity(faker.address().city());
            user.setCountry(faker.address().countryCode());
            user.setAvatar(faker.internet().avatar());
            user.setCompany(faker.company().name());
            user.setJobPosition(faker.job().title());
            user.setMobile(faker.phoneNumber().cellPhone());
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(faker.internet().password(6, 10));
            user.setRole(faker.bool().bool() ? Role.ADMIN : Role.USER);
            users.add(user);
        }
        return users;
    }

    @Override
    public String batchUsers(byte[] fileBytes) throws IOException {
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        List<User> users = objectMapper.readValue(fileBytes, new TypeReference<>() {
        });
        int total = users.size();
        int imported = 0;
        int failed = 0;
        for (User user : users) {
            if (userRepository.existsByEmailOrUsername(user.getEmail(), user.getUsername())) {
                failed++;
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                imported++;
            }
        }
        return "{ \"total\": " + total + ", \"imported\": " + imported + ", \"failed\": " + failed + " }";

    }

    @Override
    public User getYourProfile(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByUsernameOrEmail(email, email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserProfile(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
