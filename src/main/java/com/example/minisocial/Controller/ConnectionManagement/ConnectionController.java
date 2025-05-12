package com.example.minisocial.Controller.ConnectionManagement;

import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.example.minisocial.Model.UserManagement.UserDTO;
import com.example.minisocial.Model.UserManagement.UserProfileDTO;
import com.example.minisocial.Service.ConnectionManagement.ConnectionService;

import com.example.minisocial.Service.UserManagement.UserConnection;
import com.example.minisocial.Service.UserManagement.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;



@Path("/connections")
public class ConnectionController {

    @Inject
    private ConnectionService connectionService;

    @Inject
    private UserConnection userConnection;

    @Inject
    private UserService userService;


    // Send a friend request
    @POST
    @Path("/sendFriendRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendFriendRequest(@QueryParam("senderId") Long senderId, @QueryParam("receiverId") Long receiverId) {
        User sender = userConnection.findUserById(senderId);
        User receiver = userConnection.findUserById(receiverId);

        if (sender == null || receiver == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            connectionService.sendFriendRequest(sender, receiver);
            return Response.ok().build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/acceptFriendRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response acceptFriendRequest(@QueryParam("requestId") Long requestId) {
        FriendRequest request = connectionService.findFriendRequestById(requestId);

        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Friend request not found.").build();
        }

        // Check if the request is rejected, and prevent acceptance
        if ("Rejected".equalsIgnoreCase(request.getStatus())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("This friend request has already been rejected.").build();
        }

        connectionService.acceptFriendRequest(request);
        return Response.ok().build();
    }

    // Reject a friend request
    @POST
    @Path("/rejectFriendRequest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response rejectFriendRequest(@QueryParam("requestId") Long requestId) {
        FriendRequest request = connectionService.findFriendRequestById(requestId);

        if (request == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Friend request not found.").build();
        }

        // Reject the friend request
        connectionService.rejectFriendRequest(request);
        return Response.ok().build();
    }

    // Get pending friend requests
    @GET
    @Path("/pendingRequests/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingFriendRequests(@PathParam("userId") Long userId) {
        // Fetch the user by ID
        User user = userConnection.findUserById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }

        // Get the list of pending friend requests (sender's name and request ID)
        List<Object[]> pendingRequests = connectionService.getPendingFriendRequests(user);

        if (pendingRequests.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("No pending friend requests")
                    .build();
        }

        return Response.ok(pendingRequests).build();
    }

    // Suggest friends based on mutual connections
    @GET
    @Path("/suggestFriends/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response suggestFriends(@PathParam("userId") Long userId) {
        User user = userConnection.findUserById(userId);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }

        // Get the list of suggested friends
        List<Object[]> suggestedFriends = connectionService.suggestFriends(user);

        if (suggestedFriends.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("No friend suggestions available")
                    .build();
        }

        // Return the list of suggested friends (only id and name)
        return Response.ok(suggestedFriends).build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@QueryParam("searchTerm") String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Search term is required.").build();
        }

        List<UserDTO> users = connectionService.searchUsers(searchTerm); // Use UserDTO here

        if (users.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity("No users found").build();
        }

        return Response.ok(users).build();
    }



    @GET
    @Path("/view/{friendId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewFriendProfile(@PathParam("friendId") Long friendId, @QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            // Fetch the friend's profile (only name, email, bio)
            UserProfileDTO friendProfileDTO = connectionService.getFriendProfile(friendId);

            if (friendProfileDTO == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Friend profile not found").build();
            }

            return Response.ok(friendProfileDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch friend's profile: " + e.getMessage()).build();
        }
    }


    @GET
    @Path("/friends")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriends(@QueryParam("email") String email) {
        if (email == null || email.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
        }

        try {
            // Fetch the current user based on the email
            User currentUser = userService.getUserByEmail(email);

            if (currentUser == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            // Fetch the friends' names of the current user
            List<String> friendsNames = connectionService.getFriendsNames(currentUser);

            return Response.ok(friendsNames).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch friends: " + e.getMessage()).build();
        }
    }

//    @GET
//    @Path("/friends")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response viewAllFriends(@QueryParam("email") String email) {
//        if (email == null || email.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Email is required").build();
//        }
//
//        try {
//            // Fetch the current user based on email
//            User currentUser = userService.getUserByEmail(email);
//
//            if (currentUser == null) {
//                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
//            }
//
//            // Fetch all friends' names of the user
//            List<String> friendsNames = connectionService.getAllFriendsNames(currentUser.getId());
//
//            if (friendsNames == null || friendsNames.isEmpty()) {
//                return Response.status(Response.Status.NOT_FOUND).entity("No friends found").build();
//            }
//
//            return Response.ok(friendsNames).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to fetch friends: " + e.getMessage()).build();
//        }
//    }














    // GET /posts/friend/{requesterId}/{friendId}
    @GET
    @Path("/friend/{requesterId}/{friendId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFriendPosts(@PathParam("requesterId") Long requesterId,
                                   @PathParam("friendId") Long friendId) {
        try {
            List<Post> posts = connectionService.getFriendPosts(requesterId, friendId);
            return Response.ok(posts).build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You are not allowed to view this user's posts.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving posts.")
                    .build();
        }
    }
}

