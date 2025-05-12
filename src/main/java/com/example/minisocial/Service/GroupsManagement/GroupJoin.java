package com.example.minisocial.Service.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class GroupJoin {

    @Inject
    private GroupService groupService;

    public void joinGroup(Long userId, Long groupId) {
        User user = groupService.findUser(userId);
        Group group = groupService.findGroup(groupId);
        System.out.println("DEBUG >> Group ID: " + group.getId() + ", Name: " + group.getName() + ", isOpen: " + group.isOpen());

        if (user == null || group == null) throw new IllegalArgumentException("User or Group not found");

        // Closed group: create a join request

        if (!group.isOpen()) {
            if (!groupService.hasPendingRequest(user, group)) {
                groupService.createJoinRequest(user, group);
            } else {
                throw new IllegalStateException("You already have a pending request.");
            }
            return; // Don't auto-join
        }

        // Open group: join immediately
        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            groupService.update(group);
        }
    }

    public Group getGroup(Long id) {
        if (id != null) {
            return groupService.findGroup(id);
        }
        return null;
    }
    //handle name when user join
    public String getUserName(Long userId) {
        if (userId != null) {
            User user = groupService.findUser(userId);
            if (user != null) {
                return user.getName();
            }
        }
        return "Unknown User";
    }




}