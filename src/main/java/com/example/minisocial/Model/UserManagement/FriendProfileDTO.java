package com.example.minisocial.Model.UserManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.List;

public class FriendProfileDTO {
    private UserProfileDTO friendProfile;
    private List<Post> posts;

    public FriendProfileDTO(UserProfileDTO friendProfile, List<Post> posts) {
        this.friendProfile = friendProfile;
        this.posts = posts;
    }

    // Getters and Setters
    public UserProfileDTO getFriendProfile() {
        return friendProfile;
    }

    public void setFriendProfile(UserProfileDTO friendProfile) {
        this.friendProfile = friendProfile;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
