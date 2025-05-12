package com.example.minisocial.Service.UserManagement;

import com.example.minisocial.Model.UserManagement.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Base64;
import java.util.Date;

@Stateless
public class UserLoginService {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    private static final String SECRET_KEY = "J7PoPDMZjVZLKDQ/z65nslM/yPnX4W3Kq30YhKq26tKIoPXPDxUj0Sve77jOqKkck0nvYe2/6sEbMjYeX/WmnQ==";  // Secret key for JWT signing

    // Method to authenticate the user and generate JWT token
    public String loginUser(String email, String password) {
        // Find user by email
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }


        try {
            String decodedPassword = new String(Base64.getUrlDecoder().decode(user.getPassword()));
            if (!password.equals(decodedPassword)) {
                throw new IllegalArgumentException("Invalid email or password.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        // Generate JWT token
        return generateJwtToken(user);
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())  // Add the role to the token
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS512, Base64.getDecoder().decode(SECRET_KEY)) // Decode the Base64 key
                .compact();
    }

}
