package com.example.minisocial.Service.GroupsManagement;


import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.JoinRequest;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.*;

import java.util.List;

//crud operation
    @Stateless
    public class GroupService {

    @Inject
    private GroupCreation groupCreation;

        @PersistenceContext(unitName = "myPersistenceUnit")
        private EntityManager em;

        public void save(Group group) {
            em.persist(group);
        }

        public void update(Group group) {
            em.merge(group);
        }
//for lazy initialize
        public Group findGroup(Long id) {
            return em.createQuery(
                            "SELECT g FROM Group g LEFT JOIN FETCH g.members WHERE g.id = :id", Group.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }


        public User findUser(Long id) {
            return em.find(User.class, id);
        }
        public Group getGroup(Long id) {
        return findGroup(id);
    }


        //make sure he is an admin
        public String removeUserFromGroup(Long groupId, Long adminId, Long userId) {
            Group group = findGroup(groupId);
            User userToRemove = findUser(userId);

            if (group == null || userToRemove == null)
                throw new IllegalArgumentException("Group or User not found");

            if (!groupCreation.isGroupAdmin(groupId, adminId))
                throw new SecurityException("Only admins can remove users");

            if (!group.getMembers().contains(userToRemove))
                throw new IllegalArgumentException("User is not a member");

            group.getMembers().remove(userToRemove);
            group.getAdmins().remove(userToRemove);
            update(group);

            return userToRemove.getName();
        }

    public void deleteGroup(Long groupId, Long adminId) {
        Group group = findGroup(groupId);
        User admin = findUser(adminId);

        if (group == null || admin == null)
            throw new IllegalArgumentException("Group or Admin not found");

        if (!groupCreation.isGroupAdmin(groupId, adminId))
            throw new SecurityException("Only admins can delete the group");

        group = em.merge(group); // Ensure it's managed
        em.remove(group);
    }
    public List<JoinRequest> getPendingRequests(Long groupId) {
        return em.createQuery(
                        "SELECT jr FROM JoinRequest jr WHERE jr.group.id = :groupId AND jr.status = :status", JoinRequest.class)
                .setParameter("groupId", groupId)
                .setParameter("status", JoinRequest.Status.PENDING)
                .getResultList();
    }
    public boolean hasPendingRequest(User user, Group group) {
        String jpql = "SELECT COUNT(jr) FROM JoinRequest jr " +
                "WHERE jr.user = :user AND jr.group = :group AND jr.status = :status";
        Long count = em.createQuery(jpql, Long.class)
                .setParameter("user", user)
                .setParameter("group", group)
                .setParameter("status", JoinRequest.Status.PENDING)
                .getSingleResult();
        return count > 0;
    }

    public void createJoinRequest(User user, Group group) {
        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setGroup(group);
        request.setStatus(JoinRequest.Status.PENDING);
        em.persist(request);
    }

    public JoinRequest findJoinRequest(Long requestId) {
        return em.find(JoinRequest.class, requestId);
    }
    public void updateJoinRequest(JoinRequest request) {
        em.merge(request);
    }


//for post
public List<Post> findPostsByGroupId(Long groupId) {
    return em.createQuery(
                    "SELECT DISTINCT p FROM Post p " +
                            "LEFT JOIN FETCH p.postContents " +
                            "WHERE p.group.id = :groupId", Post.class)
            .setParameter("groupId", groupId)
            .getResultList();
}

//post
    public void removePostFromGroup(Long groupId, Long adminId, Long postId) {
        Group group = findGroup(groupId);
        User admin = findUser(adminId);

        if (group == null || admin == null) {
            throw new IllegalArgumentException("Group or Admin not found");
        }

        Post post = em.find(Post.class, postId);
        if (post == null || post.getGroup() == null || !post.getGroup().getId().equals(groupId)) {
            throw new IllegalArgumentException("Post not found or does not belong to the group");
        }

        // ✅ Check if the admin is actually an admin of the group
        boolean isGroupAdmin = group.getAdmins().contains(admin);
        if (!isGroupAdmin) {
            throw new SecurityException("Only group admins can remove posts");
        }

        // ✅ Remove post from group list and database
        group.getPosts().remove(post);
        em.remove(post);
        update(group); // optional consistency step
    }

    public void handleJoinRequest(Long requestId, Long adminId, boolean approve) {
        JoinRequest request = findJoinRequest(requestId);
        if (request == null) throw new IllegalArgumentException("Join request not found");

        Group group = request.getGroup();
        User admin = findUser(adminId);

        if (group == null || admin == null || !group.getAdmins().contains(admin)) {
            throw new SecurityException("Only group admins can handle join requests");
        }

        if (approve) {
            request.setStatus(JoinRequest.Status.APPROVED);
            group.getMembers().add(request.getUser());
        } else {
            request.setStatus(JoinRequest.Status.REJECTED);
        }

        updateJoinRequest(request);
        update(group);
    }


}

