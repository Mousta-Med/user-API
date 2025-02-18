package com.med.userapi.entity;

import com.med.userapi.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String city;
    private String country;
    private String avatar;
    private String company;
    private String jobPosition;
    private String mobile;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
