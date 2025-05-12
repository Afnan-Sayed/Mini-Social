package com.example.minisocial.Model.UserManagement;

public class UserDTOForPost {
    private String name;
    private String email;
    private String bio;

    public UserDTOForPost(String name, String email, String bio) {
        this.name = name;
        this.email = email;
        this.bio = bio;
    }
    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}