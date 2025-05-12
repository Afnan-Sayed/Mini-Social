//package com.example.minisocial.Service.PostManagement.Post;
//
//import com.example.minisocial.Model.PostManagement.Post.Post;
//import jakarta.ejb.Stateless;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.util.List;
//
//@Stateless
//public class FeedService
//{
//    @PersistenceContext
//    private EntityManager em;
//
//    //fetch paginated feed (top 3 posts from each one of friends and of the user at a time)
//    public List<Post> getUserFeed(Long userId, int offset, int limit) {
//        return em.createQuery(
//                        "SELECT p FROM Post p WHERE p.userID = :userId " +
//                                "ORDER BY p.timestamp DESC", Post.class)
//                .setParameter("userId", userId)
//                .setFirstResult(offset)  // Set the offset (starting point)
//                .setMaxResults(limit)    // Set the limit (number of posts to fetch)
//                .getResultList();
//    }
//}
//
///*
//// Get post by ID
//    public Post getPost(Long postID) {
//        return entityManager.find(Post.class, postID);
//    }
//
//    // Update post
//    public Post updatePost(Long postID, Post post) {
//        Post existingPost = getPost(postID);
//        if (existingPost != null) {
//            // Update post properties
//            existingPostPost.setContent(post.getContent());
//            existingPost.setTimestamp(post.getTimestamp() != null ? post.getTimestamp() : LocalDateTime.now()); // Use provided timestamp or current time
//            existingPost.setStatus(post.getStatus());
//            existingPost.setAuthor(post.getAuthor());
//
//            // Update post content (this assumes content is being replaced)
//            for (PostContent content : post.getPostContents()) {
//                if (content.getContentValue() != null && !content.getContentValue().isEmpty()) {
//                    content.setPost(existingPost);
//                    entityManager.merge(content);  // Merge content changes
//                }
//            }
//
//            return entityManager.merge(existingPost);
//        }
//        return null;
//    }
//
//        //get all posts
//    public List<Post> getAllPosts() {
//        return entityManager.createQuery("SELECT p FROM Post p", Post.class).getResultList();
//    }
//
// */