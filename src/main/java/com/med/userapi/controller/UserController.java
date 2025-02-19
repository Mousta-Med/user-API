package com.med.userapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.med.userapi.entity.User;
import com.med.userapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/generate")
    public ResponseEntity<List<User>> getUsers(@RequestParam int count) {
        List<User> users = userService.generateUsers(count);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "users.json");
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String response = userService.batchUsers(file.getBytes());
        return ResponseEntity.ok(response);
    }
}
