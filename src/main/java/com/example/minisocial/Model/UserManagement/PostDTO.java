package com.example.minisocial.Model.UserManagement;


import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.UserDTOForPost;
import java.util.List;

public class PostDTO {
    private Long postId;
    private String status;
    private UserDTOForPost author;
    private List<PostContent> postContents;

    public PostDTO(Long postId, String status, UserDTOForPost author, List<PostContent> postContents) {
        this.postId = postId;
        this.status = status;
        this.author = author;
        this.postContents = postContents;
    }

    // Getters and Setters
    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDTOForPost getAuthor() {
        return author;
    }

    public void setAuthor(UserDTOForPost author) {
        this.author = author;
    }

    public List<PostContent> getPostContents() {
        return postContents;
    }

    public void setPostContents(List<PostContent> postContents) {
        this.postContents = postContents;
    }
}