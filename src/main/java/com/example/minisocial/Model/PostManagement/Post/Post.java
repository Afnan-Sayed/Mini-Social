package com.example.minisocial.Model.PostManagement.Post;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.UserManagement.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import java.util.List;
//JPA entity bean


@Entity
public class Post
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postID;

    @ManyToOne
    @JoinColumn(name = "authorID")
    private User author;
    private String status;
    private String authorName;
    private int NumOfLikes;
    private int NumOfComments;


    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonbTransient
    private List<PostContent> postContents;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    public Post(User author, String authorName, String status, List<PostContent> postContents, Group group)
    {
        this.author = author;
        this.authorName = authorName;
        this.status = status;
        this.postContents = postContents;
        this.group = group;
    }

    public Post() {}

    // Getters and Setters
    public Long getPostId() { return postID; }

    public List<PostContent> getPostContents() { return postContents; }
    public void setPostContents(List<PostContent> postContents) { this.postContents = postContents; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public Group getGroup() { return group; }

    public Long getGroupId() {
        return (group != null) ? group.getId() : null;
    }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public int getNumOfComments() {
        return NumOfComments;
    }

    public void setNumOfComments(int numOfComments) {
        NumOfComments = numOfComments;
    }

    public int getNumOfLikes() {
        return NumOfLikes;
    }

    public void setNumOfLikes(int numOfLikes) {
        NumOfLikes = numOfLikes;
    }
}
