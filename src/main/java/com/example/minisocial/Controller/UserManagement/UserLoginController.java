package com.example.minisocial.Controller.UserManagement;


import com.example.minisocial.Model.UserManagement.User;
import com.example.minisocial.Service.UserManagement.UserLoginService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserLoginController {

    @Inject
    private UserLoginService userLoginService;

    @POST
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
}
