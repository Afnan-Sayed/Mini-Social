package com.example.minisocial.Service.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.GroupPostResp;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;
import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequestResponse;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.PostManagement.Post.PostResponse;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import java.util.List;


@Stateless
public class GroupFacade {

    @Inject
    private GroupService groupService;
    @Inject
    private GroupCreation groupCreation;
    @Inject
    private GroupJoin groupJoin;
    @Inject
    private CreationPostGroup creationPostGroup;



    public void createGroup(String name, String description, boolean isOpen, Long creatorId) {
        groupCreation.createGroup(name, description, isOpen, creatorId);
    }

    public void promoteToAdmin(Long groupId, Long adminId, Long userId) {
        groupCreation.promoteToAdmin(groupId, adminId, userId);
    }

    //only function that calls the function in the actual service class
    // edit all the following
    public void joinGroup(Long userId, Long groupId) {
        groupJoin.joinGroup(userId, groupId);
    }

    public Group getGroup(Long id) {
        return groupService.findGroup(id);
    }

    public String getUserName(Long userId) {
        return groupJoin.getUserName(userId);
    }

    public String removeUserFromGroup(Long groupId, Long adminId, Long userId) {
        return groupService.removeUserFromGroup(groupId, adminId, userId);
    }

    public boolean isGroupAdmin(Long groupId, Long userId) {
        return groupCreation.isGroupAdmin(groupId, userId);
    }

    public void deleteGroup(Long groupId, Long adminId) {
        groupService.deleteGroup(groupId, adminId);
    }

    //post
    public List<GroupPostResp> getGroupPostsForMember(Long groupId, Long userId) {
        return creationPostGroup.getGroupPostsForMember(groupId, userId);
    }

    public void handleJoinRequest(Long requestId, Long adminId, boolean approve) {
        groupService.handleJoinRequest(requestId, adminId, approve);
    }

    public List<JoinRequestResponse> getPendingJoinRequests(Long groupId) {
        return groupService.getPendingRequests(groupId);
    }

    public void removePostFromGroup(Long groupId, Long adminId, Long postId) {
        groupService.removePostFromGroup(groupId, adminId, postId);
    }
    public void leaveGroup(Long groupId, Long userId) {
        groupService.leaveGroup(groupId, userId);
    }

}
