package com.med.userapi.controller;

import com.med.userapi.entity.User;
import com.med.userapi.entity.dto.AuthenticationReq;
import com.med.userapi.entity.dto.AuthenticationRes;
import com.med.userapi.service.AuthenticationService;
import com.med.userapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/users/generate")
    public ResponseEntity<List<User>> getUsers(@RequestParam int count) {
        List<User> users = userService.generateUsers(count);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "users.json");
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @PostMapping(value = "/users/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> batchUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String response = userService.batchUsers(file.getBytes());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthenticationRes> login(@RequestBody @Valid AuthenticationReq authenticationReq) {
        return ResponseEntity.ok(authenticationService.login(authenticationReq));
    }

    @GetMapping("/users/me")
    public ResponseEntity<User> getYourProfile(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getYourProfile(request));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/users/{username}")
    public ResponseEntity<User> getUserProfile(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserProfile(username));
    }

}
