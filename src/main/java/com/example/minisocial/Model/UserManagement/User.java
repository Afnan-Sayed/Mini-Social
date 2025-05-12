package com.example.minisocial.Model.UserManagement;

import jakarta.persistence.*;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String bio;
    private String role;


    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<FriendRequest> sentRequests;  // Sent friend requests

//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonbTransient
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<FriendRequest> receivedRequests;  // Received friend requests

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private List<com.example.minisocial.Model.UserManagement.User> friends;  // List of friends

    // Getters and setters

    public User(String email, String password, String name, String bio, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.role = role;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<com.example.minisocial.Model.UserManagement.User> getFriends() {
        return friends;
    }

    public void setFriends(List<com.example.minisocial.Model.UserManagement.User> friends) {
        this.friends = friends;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
