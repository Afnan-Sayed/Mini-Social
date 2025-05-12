package com.example.minisocial.Authentication;

import io.jsonwebtoken.Jwts;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ResourceInfo;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Provider
@PreMatching
public class JWTFilter implements ContainerRequestFilter {

    private static final String SECRET_KEY = "J7PoPDMZjVZLKDQ/z65nslM/yPnX4W3Kq30YhKq26tKIoPXPDxUj0Sve77jOqKkck0nvYe2/6sEbMjYeX/WmnQ=="; // Secret key for signing JWT

    @Context
    private ResourceInfo resourceInfo;  // Inject ResourceInfo to access method and class annotations

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Check if the class or method has the @JWTRequired annotation
        if (!isJWTRequired(requestContext)) {
            return; // Skip filter if no JWT validation is required
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract token
            try {
                // Create JwtParser using JwtParserBuilder
                JwtParser jwtParser = Jwts.parser()
                        .setSigningKey(Base64.getDecoder().decode(SECRET_KEY)) // Decode the Base64 secret key
                        .build();

                // Validate JWT and extract claims
                Claims claims = jwtParser.parseClaimsJws(token).getBody();

                // Extract role from the token and set it in the request context
                String role = claims.get("role", String.class);
                requestContext.setProperty("role", role);  // Store role for later use

                // Set user ID or email for other use cases
                String userId = claims.getSubject();  // ID
                requestContext.setProperty("userId", userId);

                String userEmail = claims.getSubject();  // Email
                requestContext.setProperty("userEmail", userEmail);

                // Check token expiration
                Date expiration = claims.getExpiration();
                if (expiration.before(new Date())) {
                    throw new Exception("Token has expired");
                }

            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    // Method to check if the class or method requires JWT validation
    private boolean isJWTRequired(ContainerRequestContext requestContext) {
        // Check for @JWTRequired annotation on method or class
        return resourceInfo.getResourceMethod().isAnnotationPresent(JWTRequired.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(JWTRequired.class);
    }
}
