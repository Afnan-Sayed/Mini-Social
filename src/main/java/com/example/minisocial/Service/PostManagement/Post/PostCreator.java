package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostCreator
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    public Post createPost(String loggedInEmail, String status, List<PostContent> postContents)
    {
        User user = userService.getUserByEmail(loggedInEmail);
        String role= user.getRole();
        if (role.equals("admin")){
            throw new SecurityException("Admins are not allowed to create posts");
        }
        //validate that at least one post content type is not null or empty
        if (postContents == null || postContents.isEmpty()) {
            throw new IllegalArgumentException("Post contents cannot be null or empty");
        }

        Post post = new Post(user, status, LocalDateTime.now(), postContents);
        // Persist content (links, images, text)
        for (PostContent content : postContents) {
            content.linkToPost(post);  // Link the content to the post
            entityManager.persist(content);  // Persist content to the database
        }

        entityManager.persist(post);
        return post;
    }
}
