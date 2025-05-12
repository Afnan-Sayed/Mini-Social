package com.example.minisocial.Model.PostManagement.Post;

import com.example.minisocial.Model.UserManagement.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
    private String authorName;


    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<PostContent> postContents;

    public Post(User author, String authorName, String status, List<PostContent> postContents)
    {
        this.author = author;
        this.authorName = authorName;
        this.status = status;
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

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}