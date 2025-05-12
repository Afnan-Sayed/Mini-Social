package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.PostManagement.Post.PostCreator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostCreationController
{
    @Inject
    private PostCreator postCreator;

    @POST
    @Path("/createPost")
    public Response createPost(Post post, @QueryParam("email") String email)
    {
        try {
            // Call PostCreator service to create the post
            Post createdPost = postCreator.createPost(email, post.getStatus(), post.getPostContents());
            return Response.status(Response.Status.CREATED).entity(createdPost).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create post: " + e.getMessage()).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity("Permission denied: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error: " + e.getMessage()).build();
        }
    }
}