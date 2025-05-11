package com.example.minisocial.Service.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.User;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.Collections;

@Stateless
public class GroupFacade {

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
        group.setMembers(Collections.singletonList(creator));
        group.setAdmins(Collections.singletonList(creator));

        groupService.save(group);
    }

    public void joinGroup(Long userId, Long groupId) {
        User user = groupService.findUser(userId);
        Group group = groupService.findGroup(groupId);
        if (user == null || group == null) throw new IllegalArgumentException("User or Group not found");
        if (!group.isOpen()) {
            throw new IllegalStateException("This group is closed. You must request to join.");
        }
        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            groupService.update(group);
        }
    }

    public Group getGroup(Long id) {
        return groupService.findGroup(id);
    }

    public String getUserName(Long userId) {
        User user = groupService.findUser(userId);
        return (user != null) ? user.getName() : "Unknown User";
    }
    public void promoteToAdmin(Long groupId, Long adminId, Long userId) {
        Group group = groupService.findGroup(groupId);
        User admin = groupService.findUser(adminId);
        User userToPromote = groupService.findUser(userId);

        if (group == null || admin == null || userToPromote == null)
            throw new IllegalArgumentException("Group or User not found");

        if (!group.getAdmins().contains(admin))
            throw new SecurityException("Only current admins can promote others");

        if (!group.getMembers().contains(userToPromote))
            throw new IllegalArgumentException("User is not a member of the group");

        if (!group.getAdmins().contains(userToPromote)) {
            group.getAdmins().add(userToPromote);
            groupService.update(group);
        }
    }


    public String removeUserFromGroup(Long groupId, Long adminId, Long userId) {
        Group group = groupService.findGroup(groupId);
        User admin = groupService.findUser(adminId);
        User userToRemove = groupService.findUser(userId);

        if (group == null || admin == null || userToRemove == null)
            throw new IllegalArgumentException("Group or User not found");

        if (!group.getAdmins().contains(admin))
            throw new SecurityException("Only admins can remove users");

        if (!group.getMembers().contains(userToRemove))
            throw new IllegalArgumentException("User is not a member");
        if (group.getAdmins().contains(userToRemove)) {
            throw new SecurityException("Cannot remove another admin");
        }

        // Save the name before removal
        group.getMembers().remove(userToRemove);
        group.getAdmins().remove(userToRemove); // If they're admin too
        groupService.update(group);

        return userToRemove.getName();
    }

}

