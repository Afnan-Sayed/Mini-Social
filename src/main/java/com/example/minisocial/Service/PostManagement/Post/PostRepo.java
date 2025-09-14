package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PostRepo
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    //get all posts of a specific user bu email
    public List<Post> getPostsOfUser(String email)
    {
        User user =userService.getUserByEmail(email);
        Long userId=user.getId();
        String query = "SELECT p FROM Post p WHERE p.author.id = :userId";
        return entityManager.createQuery(query, Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    //delete a post by its ID
    //el id hayezhar when post is created fa da ele hna5do
    public void deletePostByAdmin(Long postId) {
        Post post = entityManager.find(Post.class, postId);
        if (post != null) {
            entityManager.remove(post);
        } else {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }
    }
    public void deletePostByUser(Long postId, String loggedInEmail) {
        Post post = entityManager.find(Post.class, postId);
        User user = userService.getUserByEmail(loggedInEmail);

        if (post != null) {
            User author = post.getAuthor();
            if (author != null && author.getEmail().equals(loggedInEmail)) {
                entityManager.remove(post);
            } else {
                throw new SecurityException("User is not allowed to delete this post.");
            }
        } else {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }
    }


    public Post updatePostByAdmin(Long postId, String newStatus, List<PostContent> newContents) {
        Post post = entityManager.find(Post.class, postId);

        if (post != null) {
            post.setStatus(newStatus);
            post.setPostContents(newContents);

            // Admin can update any post
            return entityManager.merge(post);
        } else {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }
    }
    public Post updatePostForUser(Long postId, String loggedInEmail, String newStatus, List<PostContent> newContents) {
        Post post = entityManager.find(Post.class, postId);
        User user = userService.getUserByEmail(loggedInEmail);

        if (post != null) {
            // Check if the user is the creator of the post
            User author = post.getAuthor();
            if (author != null && author.getEmail().equals(loggedInEmail)) {
                post.setStatus(newStatus);
                post.setPostContents(newContents);

                // Update the post if the user is the creator
                return entityManager.merge(post);
            } else {
                throw new SecurityException("User is not allowed to update this post.");
            }
        } else {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }
    }


}