package com.example.minisocial.Model.PostManagement.Post;

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
    private Long userID; // ID of the user who posted (FK)
    private String status;
    private LocalDateTime timestamp; // Timestamp when post was created
    private String author;

    @OneToMany(cascade = CascadeType.ALL)
    private List<PostContent> postContents;

    public Post(Long postID, Long userID, String status, LocalDateTime timestamp, String author)
    {
        this.postID=postID;
        this.userID=userID;
        this.status=status;
        this.timestamp=timestamp;
        this.author=author;
    }
    public Post(){}

    // Getters and Setters
    public Long getId() { return postID; }
    public void setId(Long id) { this.postID= id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author= author; }

    public List<PostContent> getPostContents() { return postContents; }
    public void setPostContents(List<PostContent> postContents) { this.postContents= postContents; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status= status; }

    public Long getUserId() { return userID; }
    public void setUserId(Long userId) { this.userID = userId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}