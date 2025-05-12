package com.example.minisocial.Service.PostManagement.Post;


import com.example.minisocial.Model.PostManagement.Engagement.Comment;
import com.example.minisocial.Model.PostManagement.Engagement.Like;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Stateless
public class EngRepo
{
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;
    public Like addLike(Post post, User user)
    {
        Like like = new Like(post, user);
        entityManager.persist(like);
        return like;
    }

    // Add a comment to a post
    public Comment addComment(Post post, User user, String content)
    {
        Comment comment = new Comment(post, user, content);
        entityManager.persist(comment);
        return comment;
    }

}