package com.example.minisocial.Service.PostManagement.Post;
//package com.example.minisocial.Service.PostManagement.Post;
//
//import com.example.minisocial.Model.PostManagement.Post.Post;
//import jakarta.ejb.Stateless;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//@Stateless
//public class PostRepo
//{
//    @PersistenceContext(unitName = "myPersistenceUnit")
//    private EntityManager entityManager;
//
//    //save post to the database
//    public Post savePost(Post post)
//    {
//        entityManager.persist(post);
//        return post;
//    }
//
//    // More database operations like find, delete, etc.
//    // Delete post
//    public boolean deletePost(Long postID)
//    {
//        Post post = getPost(postID);
//        if (post != null)
//        {
//            entityManager.remove(post);
//            return true;
//        }
//        return false;
//    }
//}