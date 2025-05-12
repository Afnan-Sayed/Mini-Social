package com.example.minisocial.Model.GroupsManagement;


import com.example.minisocial.Model.PostManagement.Post.PostContent;

import java.util.List;

public class GroupPostResp {
    private Long postId;
    private String authorName;
    private String status;
    private List<PostContent> postContents;

    public GroupPostResp(Long postId, String authorName, String status, List<PostContent> postContents) {
        this.postId = postId;
        this.authorName = authorName;
        this.status = status;
        this.postContents = postContents;
    }

    // Getters and Setters
    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<PostContent> getPostContents() { return postContents; }
    public void setPostContents(List<PostContent> postContents) { this.postContents = postContents; }
}
