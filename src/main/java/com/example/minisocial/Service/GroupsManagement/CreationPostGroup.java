package com.example.minisocial.Service.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.GroupPostResp;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostContent;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CreationPostGroup {

    @Inject
    private GroupService groupService;

    public List<GroupPostResp> getGroupPostsForMember(Long groupId, Long userId) {
        Group group = groupService.findGroup(groupId);
        User user = groupService.findUser(userId);

        if (group == null || user == null) {
            throw new IllegalArgumentException("Group or user not found");
        }

        if (!group.getMembers().contains(user)) {
            throw new SecurityException("You are not a member of this group");
        }

        List<Post> posts = groupService.findPostsByGroupId(groupId);

        List<GroupPostResp> responseList = new ArrayList<>();
        for (Post post : posts) {
            responseList.add(new GroupPostResp(
                    post.getPostId(),
                    post.getAuthorName(),
                    post.getStatus(),
                    post.getPostContents() // يجب أن يكون محملاً مسبقًا بـ JOIN FETCH
            ));
        }

        return responseList;
    }
}



/*
    //view post only members of group
    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager em;

    public List<Post> getGroupPostsForMember(Long groupId, Long userId) {
        Group group = groupService.findGroup(groupId);
        User user = groupService.findUser(userId);

        if (group == null || user == null) {
            throw new IllegalArgumentException("Group or user not found");
        }

        if (!group.getMembers().contains(user)) {
            throw new SecurityException("You are not a member of this group");
        }

        // Use JPQL to fetch posts for the group
        return em.createQuery(
                        "SELECT p FROM Post p WHERE p.group.id = :groupId", Post.class)
                .setParameter("groupId", groupId)
                .getResultList();
    }*/






