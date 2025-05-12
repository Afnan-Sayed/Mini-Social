package com.example.minisocial.Model.UserManagement;


import com.example.minisocial.Model.PostManagement.Post.Post;
import java.util.List;

public class FriendProfileDTO {
    private UserDTO user;
    private List<Post> posts;

    public FriendProfileDTO(UserDTO user, List<Post> posts) {
        this.user = user;
        this.posts = posts;
    }

    // Getters and Setters
    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
