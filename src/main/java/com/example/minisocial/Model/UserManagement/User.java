package com.example.minisocial.Model.UserManagement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;
import jakarta.persistence.*;

import jakarta.persistence.*;
import java.util.List;
import jakarta.json.bind.annotation.JsonbTransient;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

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
    //new
    @ManyToMany(mappedBy = "members", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Group> groups;

    @ManyToMany(mappedBy = "adminOfGroups",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Group> adminOfGroups;



    public User() {
    }

    public User(String email, String password, String name, String bio, String role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.bio = bio;
        this.role = role;
    }

    //new
    // Getters and Setters
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getAdminOfGroups() {
        return adminOfGroups;
    }

    public void setAdminOfGroups(List<Group> adminOfGroups) {
        this.adminOfGroups = adminOfGroups;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<JoinRequest> joinRequests;
    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    public void setJoinRequests(List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }




    /////////////////////////////
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
