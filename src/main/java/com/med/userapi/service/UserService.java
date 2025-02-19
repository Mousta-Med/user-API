package com.med.userapi.service;

import com.med.userapi.entity.User;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> generateUsers(int count);
    String batchUsers(byte[] fileBytes) throws IOException;
}
