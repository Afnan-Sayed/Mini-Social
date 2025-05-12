package com.example.minisocial.Model.UserManagement;


import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfileDTO {
    private String name;
    private String email;
    private String bio;

    public UserProfileDTO(String name, String email, String bio) {
        this.name = name;
        this.email = email;
        this.bio = bio;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}