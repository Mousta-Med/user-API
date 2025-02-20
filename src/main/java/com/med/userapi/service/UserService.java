package com.med.userapi.service;

import com.med.userapi.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> generateUsers(int count);

    String batchUsers(byte[] fileBytes) throws IOException;

    User getYourProfile(HttpServletRequest request);

    User getUserProfile(String username);
}
