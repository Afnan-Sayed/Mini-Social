package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.inject.Inject;
import java.util.List;

@Stateless
public class PostCreator
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private UserService userService;

    public Post createPost(String loggedInEmail, String status, List<PostContent> postContents, Long groupId)
    {
        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        User user = userService.getUserByEmail(loggedInEmail);

        if (user.getRole().equalsIgnoreCase("admin")) {
            throw new SecurityException("Admins are not allowed to create posts");
        }

        //at least one post content is not null or empty
        if (postContents == null || postContents.isEmpty()) {
            throw new IllegalArgumentException("Post contents cannot be null or empty");
        }

        Group group = null;
        if (groupId != null) {
            group = entityManager.find(Group.class, groupId);
        }

        Post post = new Post(user, user.getName(), status, postContents, group);

        for (PostContent content : postContents) {
            content.linkToPost(post);
            entityManager.persist(content);
        }

        // Persist the post object in the database
        entityManager.persist(post);

        // Return the created post
        return post;
    }
}
