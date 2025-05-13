package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Service.PostManagement.Post.EngRepo;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/engagement")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EngagementController
{
    @Inject
    private EngRepo engagementRepo;

    @POST
    @Path("/likePost")
    public Response likePost(@QueryParam("postId") Long postId, @QueryParam("email") String email)
    {
        try {
            String result = engagementRepo.addLike(postId, email);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to like post: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error: " + e.getMessage()).build();
        }
    }


    @POST
    @Path("/commentOnPost")
    public Response commentOnPost(@QueryParam("postId") Long postId, @QueryParam("email") String email, String content) {
        try {
            String result = engagementRepo.addComment(postId, email, content);
            return Response.status(Response.Status.CREATED).entity(result).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to comment on post: " + e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unexpected error: " + e.getMessage()).build();
        }
    }
}
