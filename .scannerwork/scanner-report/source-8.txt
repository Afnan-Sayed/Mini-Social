package com.example.minisocial.Controller.UserManagement;

import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserRegistrationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRegistrationController {

    @Inject
    private UserRegistrationService userRegistrationService;

    @POST
    public Response registerUser(User user) {
        try {
            userRegistrationService.registerUser(user.getEmail(), user.getPassword(), user.getName(), user.getBio(), user.getRole());
            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }


}
