package com.example.social.model;

import com.example.social.domain.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class User {
    private String id;

    private String name;

    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    private String password;

    private AuthProvider provider;

    private String providerId;

}
