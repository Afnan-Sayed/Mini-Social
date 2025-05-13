package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Model.PostManagement.Engagement.Comment;
import com.example.minisocial.Model.PostManagement.Engagement.Like;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/engagement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EngagementController
{
    @Inject
    private com.example.minisocial.Service.PostManagement.Post.EngRepo engagementRepo;

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;  // To fetch Post and User entities from the database

    // Like a post
    @POST
    @Path("/likePost")
    public Response likePost(@QueryParam("postId") Long postId, @QueryParam("userId") Long userId)
    {
        try {
            // Find the Post and User from the database
            Post post = entityManager.find(Post.class, postId);
            User user = entityManager.find(User.class, userId);

            // Check if both Post and User exist
            if (post == null || user == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Post or User not found").build();
            }

            // Add the like to the post
            Like like = engagementRepo.addLike(post, user);
            return Response.status(Response.Status.CREATED).entity(like).build();  // Return created like
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to like post: " + e.getMessage()).build();
        }
    }

    // Comment on a post
    @POST
    @Path("/commentOnPost")
    public Response commentOnPost(@QueryParam("postId") Long postId, @QueryParam("userId") Long userId, String content) {
        try {
            // Find the Post and User from the database
            Post post = entityManager.find(Post.class, postId);
            User user = entityManager.find(User.class, userId);

            // Check if both Post and User exist
            if (post == null || user == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Post or User not found").build();
            }

            // Add the comment to the post
            Comment comment = engagementRepo.addComment(post, user, content);
            return Response.status(Response.Status.CREATED).entity(comment).build();  // Return created comment
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to comment on post: " + e.getMessage()).build();
        }
    }
}