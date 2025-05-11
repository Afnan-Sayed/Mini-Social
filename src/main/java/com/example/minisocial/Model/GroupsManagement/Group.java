package com.example.minisocial.Model.GroupsManagement;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

    @Entity
    @Table(name = "app_groups")
    public class Group implements Serializable {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String description;
        private boolean isOpen;

        @ManyToOne
        @JoinColumn(name = "creator_id")
        private User creator;
//Each Group can have multiple Users as members.
//
//Each User can belong to multiple Groups (many-to-many).
        @ManyToMany
        @JoinTable(
                name = "group_members",
                joinColumns = @JoinColumn(name = "group_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private List<User> members;
        @ManyToMany
        @JoinTable(
                name = "group_admins",
                joinColumns = @JoinColumn(name = "group_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private List<User> admins;

        // Getters and Setters
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        public User getCreator() {
            return creator;
        }

        public void setCreator(User creator) {
            this.creator = creator;
        }

        public List<User> getMembers() {
            return members;
        }

        public void setMembers(List<User> members) {
            this.members = members;
        }
        public List<User> getAdmins() {
            return admins;
        }

        public void setAdmins(List<User> admins) {
            this.admins = admins;
        }


    }
