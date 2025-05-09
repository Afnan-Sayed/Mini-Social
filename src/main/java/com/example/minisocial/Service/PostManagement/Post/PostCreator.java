package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostService
{
    String status;
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    //create a post
    public Post createPost(Post post,Long loggedInUserId)
    {
        //retrieve the logged in user based on userID
        User user = entityManager.find(User.class, loggedInUserId);
        if (user == null) {
            throw new IllegalArgumentException("User not found for ID: " + loggedInUserId);
        }

        //ensure at least one PostContent is not null
        if (post.getPostContents() == null || post.getPostContents().isEmpty()){
            throw new IllegalArgumentException("Post must have at least one content type, cannot be empty");
        }

        //set the current time
        post.setTimestamp(LocalDateTime.now());

        //set the userID and author based on the logged in user's data
        post.setUserId(user.getId());
        post.setAuthor(user.getName());
        post.setStatus(status);

        //persist content (links, images, text)
        for (PostContent content : post.getPostContents())
        {
            if (content.getContentValue() == null || content.getContentValue().isEmpty()) {
                throw new IllegalArgumentException("Post content value cannot be empty");
            }
            content.setPost(post);
            entityManager.persist(content);
        }

        //persist post
        entityManager.persist(post);
        return post;
    }

    // Delete post
    public boolean deletePost(Long postID) {
        Post post = getPost(postID);
        if (post != null) {
            entityManager.remove(post);
            return true;
        }
        return false;
    }
}
