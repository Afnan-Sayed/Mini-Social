package com.example.minisocial.Model.PostManagement.Post;

import java.util.List;

public class PostResponse
{
    private String authorName;
    private String bio;
    private String status;
    private List<PostContent> postContents;
    private Long groupid;
    private Long postID;
    private int likes;
    private int comments;

    public PostResponse(
            String authorName, String status,
            List<PostContent> postContents, Long groupid,
            Long postID, String bio, int likes, int comments)
    {
        this.likes = likes;
        this.comments = comments;
        this.bio = bio;
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
    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public Long getPostID() {
        return postID;
    }

    public void setPostID(Long postID) {
        this.postID = postID;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}