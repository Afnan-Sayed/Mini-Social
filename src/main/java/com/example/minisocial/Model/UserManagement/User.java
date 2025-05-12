package com.example.minisocial.Model.UserManagement;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String bio;

    private String role;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
