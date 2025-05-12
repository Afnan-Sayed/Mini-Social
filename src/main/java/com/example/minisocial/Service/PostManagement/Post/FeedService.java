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
//public class FeedService {
//    @PersistenceContext(unitName = "myPersistenceUnit")
//    private EntityManager entityManager;
//
//    @Inject
//    private UserService userService;
//
//    public List<Post> getFeed(String loggedInEmail) {
//        User user = userService.getUserByEmail(loggedInEmail);
//
//        //top 3 posts of the logged-in user and their friends using a native SQL query with TOP 3
//        String query = "SELECT TOP 3 p.* FROM Post p " +
//                "WHERE p.authorID IN (SELECT f.friend_id FROM Friendship f WHERE f.user_id = :userId) " +
//                "ORDER BY p.postID DESC"; // You can adjust this ordering criterion
//
//        List<Post> topPosts = entityManager.createNativeQuery(query, Post.class)
//                .setParameter("userId", user.getUserId()) // Use the logged-in user's ID
//                .getResultList();
//
//        return topPosts;
//    }
//}