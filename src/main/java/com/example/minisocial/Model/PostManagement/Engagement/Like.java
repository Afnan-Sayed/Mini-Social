package com.example.minisocial.Model.PostManagement.Engagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.persistence.*;

@Entity
@Table(name = "post_like")  //avoid using the reserved keyword "Like"
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Like() {}

    public Like(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    // Getters and Setters
    public Long getLikeId() { return likeId; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}