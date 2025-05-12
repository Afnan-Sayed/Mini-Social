package com.example.minisocial.Model.GroupsManagement;
import java.util.List;

public class GroupResponse {
    public Long id;
    public String name;
    public String description;
    public Boolean isOpen;
    public Long creatorId;
    public String creatorName;
    public List<String> memberNames;
}