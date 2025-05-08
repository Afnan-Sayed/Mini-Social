package com.example.minisocial.Exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        // Check for "User not found" to return 404
        if ("User not found".equals(exception.getMessage())) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found")
                    .build();
        }
        if("Invalid email or password.".equals(exception.getMessage())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid email or password.")
                    .build();
        }
        // Default to 400 for other IllegalArgumentExceptions
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}