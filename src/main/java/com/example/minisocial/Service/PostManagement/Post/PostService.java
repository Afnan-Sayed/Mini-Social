package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PostService
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    // Create a post
    public Post createPost(Post post)
    {
        //ensure at least one PostContent is not null
        if (post.getPostContents() == null || post.getPostContents().isEmpty())
        {
            throw new IllegalArgumentException("Post must have at least one content type");
        }

        //set the current time
        post.setTimestamp(LocalDateTime.now());

        // Retrieve the logged-in user based on userID
        User user = entityManager.find(User.class, loggedInUserId);
        if (user == null) {
            throw new IllegalArgumentException("User not found for ID: " + loggedInUserId);
        }

        // Set the userID and author based on the logged-in user's data
        post.setUserId(user.getUserId());  // User ID from the logged-in user
        post.setAuthor(user.getName());    // Author name from the logged-in user


        // Persist the content (links, images, text)
        for (PostContent content : post.getPostContents()) {
            if (content.getContentValue() == null || content.getContentValue().isEmpty()) {
                throw new IllegalArgumentException("Post content value cannot be empty.");
            }
            content.setPost(post);
            entityManager.persist(content);  // Persist each content
        }

        // Persist the post
        entityManager.persist(post);
        return post;
    }

    // Get all posts
    public List<Post> getAllPosts() {
        return entityManager.createQuery("SELECT p FROM Post p", Post.class).getResultList();
    }

    // Get post by ID
    public Post getPost(Long postID) {
        return entityManager.find(Post.class, postID);
    }

    // Update post
    public Post updatePost(Long postID, Post post) {
        Post existingPost = getPost(postID);
        if (existingPost != null) {
            // Update post properties
            existingPost.setContent(post.getContent());
            existingPost.setTimestamp(post.getTimestamp() != null ? post.getTimestamp() : LocalDateTime.now()); // Use provided timestamp or current time
            existingPost.setStatus(post.getStatus());
            existingPost.setAuthor(post.getAuthor());

            // Update post content (this assumes content is being replaced)
            for (PostContent content : post.getPostContents()) {
                if (content.getContentValue() != null && !content.getContentValue().isEmpty()) {
                    content.setPost(existingPost);
                    entityManager.merge(content);  // Merge content changes
                }
            }

            return entityManager.merge(existingPost);
        }
        return null;
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
