//package com.example.minisocial.Controller;
//
//import jakarta.ws.rs.core.Response;
//import org.jboss.logging.Logger;
//import com.example.minisocial.Model.FriendRequest;
//import com.example.minisocial.Model.User;
//import com.example.minisocial.Service.UserService;
//
//import jakarta.inject.Inject;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Path("/users")
//public class UserController {
//
//    @Inject
//    private UserService userService;
//
//    // User registration
//    @POST
//    @Path("/register")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response registerUser(User user) {
//        userService.createUser(user);
//        return Response.status(Response.Status.CREATED).entity(user).build();
//    }
//
//    // User search by name or email
//    @POST
//    @Path("/friendRequest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response sendFriendRequest(@QueryParam("senderId") Long senderId, @QueryParam("receiverId") Long receiverId) {
//        User sender = userService.findUserById(senderId);
//        User receiver = userService.findUserById(receiverId);
//
//        if (sender == null || receiver == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        try {
//            userService.sendFriendRequest(sender, receiver);
//            return Response.ok().build();
//        } catch (IllegalStateException e) {
//            // Return a 400 Bad Request response with the error message
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        } catch (Exception e) {
//            // Log the error and return a detailed response
//            e.printStackTrace(); // This will print the stack trace to the server log
//            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
//        }
//    }
//
//
//    // Accept friend request
//    @POST
//    @Path("/acceptFriendRequest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response acceptFriendRequest(@QueryParam("requestId") Long requestId) {
//        FriendRequest request = userService.findFriendRequestById(requestId);
//
//        if (request != null) {
//            userService.acceptFriendRequest(request);
//            return Response.ok().build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
//
//
//    @POST
//    @Path("/rejectFriendRequest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response rejectFriendRequest(@QueryParam("requestId") Long requestId) {
//        // Find the friend request by ID
//        FriendRequest request = userService.findFriendRequestById(requestId);
//
//        if (request != null) {
//            try {
//                // Call the service to reject the friend request
//                userService.rejectFriendRequest(request);
//                return Response.ok().build();
//            } catch (Exception e) {
//                // If any error occurs, return a 500 Internal Server Error
//                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An unexpected error occurred.").build();
//            }
//        } else {
//            // If the request is not found, return a 404 Not Found
//            return Response.status(Response.Status.NOT_FOUND).entity("Friend request not found.").build();
//        }
//    }
//
//    @GET
//    @Path("/friends/{userId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getAllFriends(@PathParam("userId") Long userId) {
//        // Fetch the user by ID
//        User user = userService.findUserById(userId);
//
//        if (user == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("User not found")
//                    .build();
//        }
//
//        // Get the list of friends
//        List<User> friends = userService.getAllFriends(user);
//
//        if (friends == null || friends.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT)
//                    .entity("User has no friends yet.")
//                    .build();
//        }
//
//        return Response.ok(friends).build();
//    }
//
//
//    // Endpoint to get pending friend requests (sender's name and request ID)
//    @GET
//    @Path("/pendingRequests/{userId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getPendingFriendRequests(@PathParam("userId") Long userId) {
//        // Fetch the user by ID
//        User user = userService.findUserById(userId);
//
//        if (user == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("User not found")
//                    .build();
//        }
//
//        // Get the list of pending friend requests (sender's name and request ID)
//        List<Object[]> pendingRequests = userService.getPendingFriendRequests(user);
//
//        if (pendingRequests.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT)
//                    .entity("No pending friend requests")
//                    .build();
//        }
//
//        return Response.ok(pendingRequests).build();
//    }
//
//    // UserController.java
//
//    @GET
//    @Path("/search")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response searchUsers(@QueryParam("searchTerm") String searchTerm) {
//        if (searchTerm == null || searchTerm.trim().isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST).entity("Search term is required.").build();
//        }
//
//        List<User> users = userService.searchUsers(searchTerm);
//        if (users.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT).entity("No users found").build();
//        }
//
//        return Response.ok(users).build();
//    }
//
//
//
////    @GET
////    @Path("/suggestFriends/{userId}")
////    @Produces(MediaType.APPLICATION_JSON)
////    public Response suggestFriends(@PathParam("userId") Long userId) {
////        User user = userService.findUserById(userId);
////
////        if (user == null) {
////            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
////        }
////
////        // Get the list of suggested friends
////        List<User> suggestedFriends = userService.suggestFriends(user);
////
////        if (suggestedFriends.isEmpty()) {
////            return Response.status(Response.Status.NO_CONTENT).entity("No friend suggestions available").build();
////        }
////
////        return Response.ok(suggestedFriends).build();
////    }
//
//
//    @GET
//    @Path("/suggestFriends/{userId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response suggestFriends(@PathParam("userId") Long userId) {
//        User user = userService.findUserById(userId);
//
//        if (user == null) {
//            return Response.status(Response.Status.NOT_FOUND)
//                    .entity("User not found")
//                    .build();
//        }
//
//        // Get the list of suggested friends
//        List<Object[]> suggestedFriends = userService.suggestFriends(user);
//
//        if (suggestedFriends.isEmpty()) {
//            return Response.status(Response.Status.NO_CONTENT)
//                    .entity("No friend suggestions available")
//                    .build();
//        }
//
//        // Return the list of suggested friends (only id and name)
//        return Response.ok(suggestedFriends).build();
//    }
//
//}
