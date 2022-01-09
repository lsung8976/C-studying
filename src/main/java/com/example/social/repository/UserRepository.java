package com.example.social.repository;


import com.example.social.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);
    User save(User user);

    Boolean existsByEmail(String email);

}
