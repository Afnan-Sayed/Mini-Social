package com.example.minisocial.Controller.PostManagement;


import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.PostManagement.Post.PostService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/post")
public class PostAPI
{
    @Inject
    private PostService postService;

    //create a new post with multiple links and image URLs
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(Post post)
    {
        Post createdPost = postService.createPost(post);
        return Response.status(Response.Status.CREATED).entity(createdPost).build();
    }

    //get all posts
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts()
    {
        List<Post> posts = postService.getAllPosts();
        return Response.status(Response.Status.OK).entity(posts).build();
    }

    //get a post by its id including its links and images
    @GET
    @Path("/{postID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postID") Long postID)
    {
        Post post = postService.getPost(postID);
        if (post == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
        }
        return Response.status(Response.Status.OK).entity(post).build();
    }

    //update post
    @PUT
    @Path("/{postID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("postID") Long postID, Post post)
    {
        Post updatedPost = postService.updatePost(postID, post);
        if (updatedPost == null)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
        }
        return Response.status(Response.Status.OK).entity(updatedPost).build();
    }

    //delete post
    @DELETE
    @Path("/{postID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("postID") Long postID)
    {
        boolean deleted = postService.deletePost(postID);
        if (!deleted)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Post not found").build();
        }
        return Response.status(Response.Status.OK).build();
    }
}