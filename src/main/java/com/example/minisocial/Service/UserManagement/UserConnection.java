package com.example.minisocial.Service.UserManagement;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.example.minisocial.Model.UserManagement.User;

import java.util.ArrayList;
import java.util.logging.Logger;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Stateless

public class UserConnection {


    @PersistenceContext(unitName = "myPersistenceUnit")  // Inject EntityManager
    private EntityManager em;

    // Create a new user
    @Transactional
    public User createUser(User user) {
        em.persist(user);
        return user;
    }

    // Find a user by ID
    public User findUserById(Long id) {
        return em.find(User.class, id);
    }

    // Find a user by email
    public Optional<User> findUserByEmail(String email) {
        try {
            return Optional.of(em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
