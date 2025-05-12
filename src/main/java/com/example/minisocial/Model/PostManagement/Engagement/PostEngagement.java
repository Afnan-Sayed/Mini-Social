package com.example.minisocial.Model.PostManagement.Engagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.persistence.*;

@Entity
public class PostEngagement
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long engagementId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean liked;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public PostEngagement() {}

    public PostEngagement(Post post, User user, Boolean liked, Comment comment) {
        this.post = post;
        this.user = user;
        this.liked = liked;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getEngagementId() { return engagementId; }
    public void setEngagementId(Long engagementId) { this.engagementId = engagementId; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Boolean getLiked() { return liked; }
    public void setLiked(Boolean liked) { this.liked = liked; }

    public Comment getComment() { return comment; }
    public void setComment(Comment comment) { this.comment = comment; }
}