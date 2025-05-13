package com.example.minisocial.Controller.PostManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.PostManagement.Post.PostResponse;
import com.example.minisocial.Service.PostManagement.Post.FeedService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
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
    public Response getFeed(@QueryParam("email") String email)
    {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            List<Post> topPosts = feedService.getFeed(email);

            List<PostResponse> postResponses = new ArrayList<>();

            for (Post post : topPosts)
            {
                PostResponse response = new PostResponse(
                        post.getAuthorName(),
                        post.getStatus(),
                        post.getPostContents(),
                        post.getGroup() != null ? post.getGroup().getId() : null,
                        post.getPostId(),
                        post.getAuthor().getBio(),
                        post.getNumOfLikes(),
                        post.getNumOfComments()
                );
                postResponses.add(response);
            }

            return Response.status(Response.Status.OK).entity(postResponses).build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch feed: " + e.getMessage()).build();
        }
    }
}
