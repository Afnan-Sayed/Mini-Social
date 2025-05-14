package com.example.minisocial.Authentication;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ResourceInfo;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;

@Provider
public class JWTFilter implements ContainerRequestFilter {

    private static final String SECRET_KEY = "J7PoPDMZjVZLKDQ/z65nslM/yPnX4W3Kq30YhKq26tKIoPXPDxUj0Sve77jOqKkck0nvYe2/6sEbMjYeX/WmnQ==";

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext currentSecurityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (resourceInfo.getResourceMethod() == null || !isJWTRequired()) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                JwtParser jwtParser = Jwts.parser()
                        .setSigningKey(Base64.getDecoder().decode(SECRET_KEY))
                        .build();

                Claims claims = jwtParser.parseClaimsJws(token).getBody();

                String role = claims.get("role", String.class);
                String userId = claims.getSubject();

                // Check token expiration
                Date expiration = claims.getExpiration();
                if (expiration.before(new Date())) {
                    throw new Exception("Token has expired");
                }

                // Set custom SecurityContext
                SecurityContext customSecurityContext = new SecurityContext() {
                    @Override
                    public Principal getUserPrincipal() {
                        return () -> userId;
                    }

                    @Override
                    public boolean isUserInRole(String requiredRole) {
                        return role != null && role.equalsIgnoreCase(requiredRole);
                    }

                    @Override
                    public boolean isSecure() {
                        return currentSecurityContext != null && currentSecurityContext.isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return "Bearer";
                    }
                };

                requestContext.setSecurityContext(customSecurityContext);

                // Store the user ID in request properties for later use
                requestContext.setProperty("userId", userId);

                RolesAllowed rolesAllowed = resourceInfo.getResourceMethod().getAnnotation(RolesAllowed.class);
                if (rolesAllowed == null) {
                    rolesAllowed = resourceInfo.getResourceClass().getAnnotation(RolesAllowed.class);
                }

                if (rolesAllowed != null) {
                    boolean authorized = false;
                    for (String allowedRole : rolesAllowed.value()) {
                        if (allowedRole.equalsIgnoreCase(role)) {
                            authorized = true;
                            break;
                        }
                    }

                    if (!authorized) {
                        requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                                .entity("Access denied: insufficient role").build());
                    }
                }

            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                        .entity("Access denied: invalid or expired token").build());
            }
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization token is missing").build());
        }
    }

    private boolean isJWTRequired() {
        return resourceInfo.getResourceMethod().isAnnotationPresent(JWTRequired.class) ||
                resourceInfo.getResourceClass().isAnnotationPresent(JWTRequired.class);
    }
}
