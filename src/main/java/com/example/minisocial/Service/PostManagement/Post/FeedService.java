//package com.example.minisocial.Service.PostManagement.Post;
//
//import com.example.minisocial.Model.PostManagement.Post.Post;
//import com.example.minisocial.Service.UserManagement.UserService;
//import com.example.minisocial.Model.UserManagement.User;
//import jakarta.ejb.Stateless;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.inject.Inject;
//import java.util.List;
//
//@Stateless
//public class FeedService
//{
//    @PersistenceContext(unitName = "myPersistenceUnit")
//    private EntityManager entityManager;
//
//    @Inject
//    private UserService userService;
//
//    public List<Post> getFeed(String loggedInEmail)
//    {
//        User user = userService.getUserByEmail(loggedInEmail);
//
//        //top 3 posts of the user
//        String userQuery =
//                "SELECT p FROM Post p " +
//                "WHERE p.author.id = :userId " +
//                "ORDER BY p.postID DESC";
//
//        List<Post> topPosts = entityManager.createQuery(userQuery, Post.class)
//                .setParameter("userId", user.getId())
//                .setMaxResults(3)
//                .getResultList();
//
//        //get list of friends
//        String friendsQuery = "SELECT f.friend_id FROM Friendship f WHERE f.user_id = :userId";
//        List<Long> friendsIds = entityManager.createQuery(friendsQuery, Long.class)
//                .setParameter("userId", user.getId())
//                .getResultList();
//
//        //top 3 posts for each friend
//        for (Long friendId : friendsIds)
//        {
//            String friendPostsQuery = "SELECT p FROM Post p WHERE p.author.id = :friendId ORDER BY p.postID DESC";
//            List<Post> friendPosts = entityManager.createQuery(friendPostsQuery, Post.class)
//                    .setParameter("friendId", friendId)
//                    .setMaxResults(3)
//                    .getResultList();
//            topPosts.addAll(friendPosts);
//        }
//
//        return topPosts;  //combined list from user and friends
//    }
//}