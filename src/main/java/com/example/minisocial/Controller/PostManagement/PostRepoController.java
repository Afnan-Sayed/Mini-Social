package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Authentication.JWTRequired;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.PostManagement.Post.PostRepo;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@JWTRequired
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostRepoController {
    @Inject
    private PostRepo postRepo;

    //get all posts of a specific user by email
    @RolesAllowed("admin")
    @GET
    @Path("/getPostsOfUser")
    public Response getPostsOfUser(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            List<Post> posts = postRepo.getPostsOfUser(email);
            return Response.status(Response.Status.OK).entity(posts).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch posts: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/deletePostAdmin/{postId}")
    @RolesAllowed("admin")  // Only admin can use this method
    public Response deletePostAdmin(@PathParam("postId") Long postId) {
        try {
            postRepo.deletePostByAdmin(postId);
            return Response.status(Response.Status.NO_CONTENT).build();  // 204 No Content
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete post: " + e.getMessage()).build();
        }
    }
    @DELETE
    @Path("/deletePostUser/{postId}")
    public Response deletePostUser(@PathParam("postId") Long postId, @QueryParam("email") String loggedInEmail) {
        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            postRepo.deletePostByUser(postId, loggedInEmail);
            return Response.status(Response.Status.NO_CONTENT).build();  // 204 No Content
        } catch (IllegalArgumentException | SecurityException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete post: " + e.getMessage()).build();
        }
    }


    @PUT
    @Path("/updatePostAdmin/{postId}")
    @RolesAllowed("admin")
    public Response updatePostAdmin(@PathParam("postId") Long postId, Post updatedPost) {
        try {
            Post post = postRepo.updatePostByAdmin(postId, updatedPost.getStatus(), updatedPost.getPostContents());
            return Response.status(Response.Status.OK).entity(post).build();  // 200 OK with updated post
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update post: " + e.getMessage()).build();
        }
    }
    @PUT
    @Path("/updatePostUser/{postId}")
    public Response updatePostUser(@PathParam("postId") Long postId, @QueryParam("email") String loggedInEmail, Post updatedPost) {
        if (loggedInEmail == null || loggedInEmail.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            Post post = postRepo.updatePostForUser(postId, loggedInEmail, updatedPost.getStatus(), updatedPost.getPostContents());
            return Response.status(Response.Status.OK).entity(post).build();  // 200 OK with updated post
        } catch (IllegalArgumentException | SecurityException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update post: " + e.getMessage()).build();
        }
    }


}