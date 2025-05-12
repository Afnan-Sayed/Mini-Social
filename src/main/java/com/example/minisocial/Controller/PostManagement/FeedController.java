package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.PostManagement.Post.FeedService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/feed")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeedController
{
    @Inject
    private FeedService feedService;

    @GET
    @Path("/getFeed")
    public Response getFeed(@QueryParam("email") String email) {
        // Check if the email is provided
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            List<Post> topPosts = feedService.getFeed(email);
            return Response.status(Response.Status.OK).entity(topPosts).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch feed: " + e.getMessage()).build();
        }
    }
}
