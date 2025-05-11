package com.example.minisocial.Model.PostManagement.Post;

import jakarta.persistence.*;

@Entity
public class PostContent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentID;

    private String type;  //image,link, text.
    private String contentValue; //URL or text

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Post getPost() {
        return post;
    }
    // Getters and setters
    public Long getContentID() { return contentID; }

    public String getType() { return type; }
    public void setType(String type) { this.type= type; }

    public String getContentValue() { return contentValue; }
    public void setContentValue(String contentValue) { this.contentValue= contentValue; }

    public void linkToPost(Post post) {
        this.post = post;
    }
}