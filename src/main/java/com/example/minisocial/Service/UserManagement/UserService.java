package com.example.minisocial.Service.UserManagement;

import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserService
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    public User getUserByEmail(String loggedInEmail)
    {
        //check user is eligible to create post
        User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", loggedInEmail)
                .getSingleResult();

        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + loggedInEmail);
        }
        return user;
    }
}
