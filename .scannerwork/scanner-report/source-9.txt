package com.example.minisocial.Controller.UserManagement;


import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserUpdateProfileService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/{user_id}/update")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserUpdateProfileController {

    @Inject
    UserUpdateProfileService userUpdateProfileService;

    @PUT
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
