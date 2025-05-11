package com.example.minisocial.Controller.ConnectionManagement;

import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Model.ConnectionManagement.FriendRequest;
import com.example.minisocial.Model.UserManagement.UserDTO;
import com.example.minisocial.Service.ConnectionManagement.ConnectionService;

import com.example.minisocial.Service.UserManagement.UserConnection;
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

    // Accept a friend request
//    @POST
//    @Path("/acceptFriendRequest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response acceptFriendRequest(@QueryParam("requestId") Long requestId) {
//        FriendRequest request = connectionService.findFriendRequestById(requestId);
//
//        if (request == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        connectionService.acceptFriendRequest(request);
//        return Response.ok().build();
//    }
//
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

    // Search for users by name or email
//    @GET
//    @Path("/search")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchUsers(@QueryParam("searchTerm") String searchTerm) {
//        List<User> users = connectionService.searchUsers(searchTerm);
//
//        if (users.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT).entity("No users found").build();
//        }
//
//        return Response.ok(users).build();
//    }

//    @GET
//    @Path("/search")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchUsers(@QueryParam("searchTerm") String searchTerm) {
//        if (searchTerm == null || searchTerm.trim().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Search term is required.").build();
//        }
//
//        List<User> users = connectionService.searchUsers(searchTerm);
//
//        if (users.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT).entity("No users found").build();
//        }
//
//        return Response.ok(users).build();
//    }


//    @GET
//    @Path("/search")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchUsers(@QueryParam("searchTerm") String searchTerm) {
//        if (searchTerm == null || searchTerm.trim().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Search term is required.").build();
//        }
//
//        List<User> users = connectionService.searchUsers(searchTerm);
//
//        if (users.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT).entity("No users found").build();
//        }
//
//        return Response.ok(users).build();
//    }

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



//    // Helper methods to find user and friend request by ID
//    private User findUserById(Long userId) {
//        return connectionService.findUserById(userId);
//    }
//
//    private FriendRequest findFriendRequestById(Long requestId) {
//        return connectionService.findFriendRequestById(requestId);
//    }

}

