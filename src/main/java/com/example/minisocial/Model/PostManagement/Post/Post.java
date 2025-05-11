package com.example.minisocial.Model.PostManagement.Post;

import com.example.minisocial.Model.UserManagement.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
//JPA entity bean

@Entity
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;

    @ManyToOne
    @JoinColumn(name = "authorID")
    private User author;
    private String status;
    private LocalDateTime timestamp; // Timestamp when post was created


    @OneToMany(cascade = CascadeType.ALL)
    private List<PostContent> postContents;

    public Post(User author, String status, LocalDateTime timestamp, List<PostContent> postContents)
    {
        this.author = author;
        this.status = status;
        this.timestamp = timestamp;
        this.postContents = postContents;
    }

    public Post() {}

    // Getters and Setters
    public Long getPostId() { return postID; }

    public List<PostContent> getPostContents() { return postContents; }
    public void setPostContents(List<PostContent> postContents) { this.postContents = postContents; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}