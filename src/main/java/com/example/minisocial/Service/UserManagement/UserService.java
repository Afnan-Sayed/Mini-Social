package com.example.minisocial.Service.UserManagement;


import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Model.UserManagement.User2DTO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

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
    public List<User2DTO> getAllUsers()
    {
        //check user is eligible to create post
        TypedQuery<User2DTO> users = entityManager.createQuery("SELECT new com.example.minisocial.Model.UserManagement.User2DTO(u.id,u.name,u.email,u.bio,u.role) FROM User u", User2DTO.class);

        if (users == null) {
            throw new IllegalArgumentException("No registered users found");
        }
        return users.getResultList();
    }
}