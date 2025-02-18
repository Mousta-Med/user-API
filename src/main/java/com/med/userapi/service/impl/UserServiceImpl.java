package com.med.userapi.service.impl;

import com.github.javafaker.Faker;
import com.med.userapi.entity.User;
import com.med.userapi.enums.Role;
import com.med.userapi.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

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
}
