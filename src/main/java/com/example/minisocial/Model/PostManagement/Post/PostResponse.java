package com.example.minisocial.Model.PostManagement.Post;

import java.util.List;

public class PostResponse
{
    private String authorName;
    private String status;
    private List<PostContent> postContents;
    private Long groupid;
    private Long postID;

    public PostResponse(String authorName, String status, List<PostContent> postContents, Long groupid, Long postID) {
        this.authorName = authorName;
        this.status = status;
        this.postContents = postContents;
        this.groupid = groupid;
        this.postID = postID;
    }
    //getters and setters
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PostContent> getPostContents() {
        return postContents;
    }

    public void setPostContents(List<PostContent> postContents) {
        this.postContents = postContents;
    }
}