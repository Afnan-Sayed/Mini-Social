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

    private Long authorID; // ID of the user who posted (FK)
    private String authorName;
    private String status;
    private LocalDateTime timestamp; // Timestamp when post was created


    @OneToMany(cascade = CascadeType.ALL)
    private List<PostContent> postContents;

    public Post(Long authorID, String status, LocalDateTime timestamp, String authorName)
    {
        this.authorID=authorID;
        this.authorName=authorName;
        this.status=status;
        this.timestamp=timestamp;
    }
    public Post(){}

    // Getters and Setters
    public Long getPostId() { return postID; }

    public String getAuthorName() { return authorName; }
    public void setAuthor(String author) { this.authorName= author; }

    public List<PostContent> getPostContents() { return postContents; }
    public void setPostContents(List<PostContent> postContents) { this.postContents= postContents; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status= status; }

    public Long getUserId() { return authorID; }
    public void setUserId(Long userId) { this.authorID = userId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
