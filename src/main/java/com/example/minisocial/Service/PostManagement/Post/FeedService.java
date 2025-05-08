package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Post.Post;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class FeedService
{
    @PersistenceContext
    private EntityManager em;

    // Method to fetch paginated feed (3 posts at a time)
    public List<Post> getUserFeed(Long userId, int offset, int limit) {
        return em.createQuery(
                        "SELECT p FROM Post p WHERE p.userId = :userId " +
                                "ORDER BY p.timestamp DESC", Post.class)
                .setParameter("userId", userId)
                .setFirstResult(offset)  // Set the offset (starting point)
                .setMaxResults(limit)    // Set the limit (number of posts to fetch)
                .getResultList();
    }
}
