package com.example.minisocial.Service.PostManagement.Post;

import com.example.minisocial.Model.PostManagement.Engagement.Comment;
import com.example.minisocial.Model.PostManagement.Engagement.Like;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.ConnectionManagement.ConnectionService;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class EngRepo
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Inject
    private ConnectionService cs;

    @Inject
    private UserService us;

    private boolean isFriend(User postAuthor, User user)
    {
        List<User> friends = cs.getAllFriends(user);
        return friends.contains(postAuthor);
    }

    //like to post if the post author is a friend
    public String addLike(Long postId, String email) {
        Post post = entityManager.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }

        User user = us.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        boolean isFriend = isFriend(post.getAuthor(), user);
        if (!isFriend && !(post.getAuthor().equals(user))) {
            throw new IllegalArgumentException("You are not friends with the post author");
        }

        Like like = new Like(post, user);
        int likes=post.getNumOfLikes()+1;
        post.setNumOfLikes(likes);
        entityManager.persist(like);
        return "Like added";
    }

    // Add a comment to a post if the post author is a friend
    public String addComment(Long postId, String email, String content)
    {
        Post post = entityManager.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found for ID: " + postId);
        }

        User user = us.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        boolean isFriend = isFriend(post.getAuthor(), user);
        if (!isFriend && !(post.getAuthor().equals(user))) {
            throw new IllegalArgumentException("You are not friends with the post author");
        }

        Comment comment = new Comment(post, user, content);
        entityManager.persist(comment);
        int comments=post.getNumOfComments()+1;
        post.setNumOfComments(comments);
        return "comment added";
    }
}
