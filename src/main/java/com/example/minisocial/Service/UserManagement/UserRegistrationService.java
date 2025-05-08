package com.example.minisocial.Service.UserManagement;

import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Base64;

@Stateless
public class UserRegistrationService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;


    // Method to register the user with password encoding, role validation, and email uniqueness check
    @Transactional
    public void registerUser(String email, String password, String name, String bio, String role)
    {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be empty.");
        }
        // Validate the role before proceeding
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role. Role must be 'admin' or 'user'.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        // Validate email uniqueness
        if (isEmailTaken(email)) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        // Encode the password in Base64
        String encodedPassword = Base64.getUrlEncoder().encodeToString(password.getBytes());

        // Create the User object
        User user = new User(email, encodedPassword, name, bio, role);

        // Persist the user to the database
        entityManager.persist(user);
    }

    // Method to validate the role
    private boolean isValidRole(String role) {
        return "admin".equalsIgnoreCase(role) || "user".equalsIgnoreCase(role);
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