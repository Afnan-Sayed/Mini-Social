package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.JWTService;
import com.example.minisocial.Service.PostManagement.Post.PostCreator;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostCreationController {
    @Inject
    private JWTService jwtService;

    @Inject
    private PostCreator postCreator;

    @POST
    @Path("/createPost")
    public Response createPost(Post post, @HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Missing or invalid token").build();
        }

        String token = authHeader.substring("Bearer ".length());

        String userEmail = jwtService.getEmailFromToken(token);
        if (userEmail == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        try {
            Post createdPost = postCreator.createPost(userEmail, post.getStatus(), post.getPostContents());
            return Response.status(Response.Status.CREATED).entity(createdPost).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to create post: " + e.getMessage()).build();
        }
    }
}