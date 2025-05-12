package com.example.minisocial.Controller.UserManagement;


import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserLoginService;
import com.example.minisocial.Service.UserManagement.UserRegistrationService;
import com.example.minisocial.Service.UserManagement.UserUpdateProfileService;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.*;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserLoginService userLoginService;
    @Inject
    private UserRegistrationService userRegistrationService;
    @Inject
    UserUpdateProfileService userUpdateProfileService;

    @POST
    @Path("/login")
    public Response loginUser(User user) {
        try {
            // Call service to authenticate user and generate JWT token
            String token = userLoginService.loginUser(user.getEmail(), user.getPassword());

            // Prepare response map with the message and token
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Login successful");
            responseMap.put("token", token);  // Include the JWT token

            // Return the response as JSON
            return Response.status(Response.Status.OK).entity(responseMap).build();
        } catch (IllegalArgumentException e) {
            // Handle invalid credentials (email/password mismatch)
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", e.getMessage());  // Set the error message
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMap).build();
        }
    }


    @POST
    @Path("/register")
    public Response registerUser(User user) {
        try {
            userRegistrationService.registerUser(user.getEmail(), user.getPassword(), user.getName(), user.getBio(), user.getRole());
            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


    @PUT
    @Path("/{user_id}/update")
    public Response updateUserProfile(@PathParam("user_id") long userId, User updatedUser)
    {
        try {
            // Call the service to update the user's profile with the updated information
            userUpdateProfileService.updateUserProfile(userId, updatedUser);

            // Return success response
            return Response.status(Response.Status.OK)
                    .entity("Profile updated successfully")
                    .build();
        } catch (IllegalArgumentException e) {
            // Handle case where user doesn't exist
            // We want to return 404 when the user is not found
            if ("User not found".equals(e.getMessage())) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("User not found") // Direct string message
                        .build();
            }

            // Handle other errors (e.g., validation issues)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage()) // Send the exception message
                    .build();
        }
    }




}