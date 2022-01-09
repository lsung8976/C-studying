package com.example.social.repository;

import com.example.social.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final MongoOperations mongoOperations;
    private final String collectionName = "users";
    private final Class<User> entityClass = User.class;

    @Autowired
    public UserRepositoryImpl(MongoOperations mongoOperations) {
        Assert.notNull(mongoOperations, "MongoOperations must not be null");
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<User> findById(String id) {
        User user = mongoOperations.findById(id.toString(), entityClass, collectionName);
        if (user == null) { return Optional.empty(); }
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = mongoOperations.findOne(new Query(Criteria.where("email").is(email)), entityClass, collectionName);
        if (user == null) { return Optional.empty(); }
        return Optional.of(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return mongoOperations.exists(new Query(Criteria.where("email").is(email)), entityClass, collectionName);
    }

    @Override
    public User save(User user) {
        user = mongoOperations.save(user, collectionName);
        return user;
    }

}
