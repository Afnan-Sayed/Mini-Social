package com.example.minisocial.Service.UserManagement;


import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Base64;

@Stateless
public class UserUpdateProfileService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Transactional
    public void updateUserProfile(long userId, User updatedUser)
    {
        User user= entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (updatedUser.getEmail() == null || updatedUser.getEmail() .trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        // Validate email uniqueness
        if (!updatedUser.getEmail().equals(user.getEmail()) && isEmailTaken(updatedUser.getEmail() )) {
            throw new IllegalArgumentException("Email is already registered.");
        }
        user.setEmail(updatedUser.getEmail());

        if (updatedUser.getName()== null || updatedUser.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        user.setName(updatedUser.getName());

        if (updatedUser.getPassword() == null || updatedUser.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        // Encode the password in Base64
        String encodedPassword = Base64.getUrlEncoder().encodeToString(updatedUser.getPassword().getBytes());
        user.setPassword(encodedPassword);

        user.setBio(updatedUser.getBio());


        // merge the user to the database
        entityManager.merge(user);

    }

    // Check if the email is already taken
    private boolean isEmailTaken(String email) {
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
        return user != null;  // If the user already exists, the email is taken
    }
}
