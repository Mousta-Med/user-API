package com.med.userapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.med.userapi.entity.User;
import com.med.userapi.enums.Role;
import com.med.userapi.jwt.JWTUtil;
import com.med.userapi.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGenerateUsers() {
        int count = 5;
        List<User> expectedUsers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setBirthDate(LocalDate.of(1990, 1, 1));
            user.setCity("New York");
            user.setCountry("US");
            user.setAvatar("avatar.jpg");
            user.setCompany("ABC Inc.");
            user.setJobPosition("Software Engineer");
            user.setMobile("123-456-7890");
            user.setUsername("johndoe");
            user.setPassword("password");
            user.setRole(Role.USER);
            expectedUsers.add(user);
        }
        List<User> actualUsers = userService.generateUsers(count);
        assertEquals(expectedUsers.size(), actualUsers.size());
    }

    @Test
    void testGetUserProfile() {
        String username = "johndoe";
        User expectedUser = new User();
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        expectedUser.setCity("New York");
        expectedUser.setCountry("US");
        expectedUser.setAvatar("avatar.jpg");
        expectedUser.setCompany("ABC Inc.");
        expectedUser.setJobPosition("Software Engineer");
        expectedUser.setMobile("123-456-7890");
        expectedUser.setUsername(username);
        expectedUser.setPassword("password");
        expectedUser.setRole(Role.USER);
        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.getUserProfile(username);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testGetUserProfileNotFound() {
        String username = "johndoe";
        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserProfile(username));
    }

    @Test
    void testGetYourProfile() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String token = "token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        String email = "johndoe@example.com";
        when(jwtUtil.extractUsername(token)).thenReturn(email);
        User expectedUser = new User();
        expectedUser.setFirstName("John");
        expectedUser.setLastName("Doe");
        expectedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        expectedUser.setCity("New York");
        expectedUser.setCountry("US");
        expectedUser.setAvatar("avatar.jpg");
        expectedUser.setCompany("ABC Inc.");
        expectedUser.setJobPosition("Software Engineer");
        expectedUser.setMobile("123-456-7890");
        expectedUser.setUsername("johndoe");
        expectedUser.setPassword("password");
        expectedUser.setRole(Role.USER);
        when(userRepository.findByUsernameOrEmail(email, email)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.getYourProfile(request);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void testBatchUsers() throws IOException {
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setCity("New York");
        user.setCountry("US");
        user.setAvatar("avatar.jpg");
        user.setCompany("ABC Inc.");
        user.setJobPosition("Software Engineer");
        user.setMobile("123-456-7890");
        user.setUsername("johndoe");
        user.setEmail("johndoe@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        users.add(user);
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String jsonContent = objectMapper.writeValueAsString(users);
        byte[] fileBytes = jsonContent.getBytes();
        when(userRepository.existsByEmailOrUsername(anyString(), anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        String result = userService.batchUsers(fileBytes);
        assertTrue(result.contains("\"total\": 1"));
        assertTrue(result.contains("\"imported\": 1"));
        assertTrue(result.contains("\"failed\": 0"));
    }


    @Test
    void testBatchUsersFailed() throws IOException {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(LocalDate.of(1990, 1, 1));
        user1.setCity("New York");
        user1.setCountry("US");
        user1.setAvatar("avatar.jpg");
        user1.setCompany("ABC Inc.");
        user1.setJobPosition("Software Engineer");
        user1.setMobile("123-456-7890");
        user1.setUsername("johndoe");
        user1.setEmail("johndoe@example.com");
        user1.setPassword("password");
        user1.setRole(Role.USER);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setBirthDate(LocalDate.of(1995, 5, 10));
        user2.setCity("Los Angeles");
        user2.setCountry("US");
        user2.setAvatar("avatar2.jpg");
        user2.setCompany("XYZ Inc.");
        user2.setJobPosition("Manager");
        user2.setMobile("987-654-3210");
        user2.setUsername("janedoe");
        user2.setEmail("janedoe@example.com");
        user2.setPassword("password123");
        user2.setRole(Role.ADMIN);

        users.add(user1);
        users.add(user2);

        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        String jsonContent = objectMapper.writeValueAsString(users);
        byte[] fileBytes = jsonContent.getBytes();

        when(userRepository.existsByEmailOrUsername("johndoe@example.com", "johndoe")).thenReturn(true);
        when(userRepository.existsByEmailOrUsername("janedoe@example.com", "janedoe")).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user2);

        String result = userService.batchUsers(fileBytes);
        assertTrue(result.contains("\"total\": 2"));
        assertTrue(result.contains("\"imported\": 1"));
        assertTrue(result.contains("\"failed\": 1"));
    }


}

