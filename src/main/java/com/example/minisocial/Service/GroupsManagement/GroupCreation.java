package com.example.minisocial.Service.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;
import com.example.minisocial.Model.GroupsManagement.JoinRequest.Status;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class GroupCreation
{
    @Inject
    private GroupService groupService;

    public void createGroup(String name, String description, boolean isOpen, Long creatorId) {
        User creator = groupService.findUser(creatorId);
        if (creator == null) throw new IllegalArgumentException("Creator not found");

        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setOpen(isOpen);
        group.setCreator(creator);

        List<User> members = new ArrayList<>();
        members.add(creator);
        group.setMembers(members);

        List<User> admins = new ArrayList<>();
        admins.add(creator);
        group.setAdmins(admins);

        groupService.save(group);
    }

    public void promoteToAdmin(Long groupId, Long adminId, Long userIdToPromote) {
        if (!isGroupAdmin(groupId, adminId))
            throw new SecurityException("Only admins can promote");

        Group group = groupService.findGroup(groupId);
        User user = groupService.findUser(userIdToPromote);

        if (group == null || user == null)
            throw new IllegalArgumentException("Group or User not found");

        if (!group.getMembers().contains(user))
            throw new IllegalArgumentException("User is not a member");

        if (!group.getAdmins().contains(user)) {
            group.getAdmins().add(user);
            groupService.update(group);
        }
    }

    public boolean isGroupAdmin(Long groupId, Long userId) {
        Group group = groupService.findGroup(groupId);
        User user = groupService.findUser(userId);
        return group != null && user != null && group.getAdmins().contains(user);
    }


}