package com.example.minisocial.NotificationsManagement;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/activity-log")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityLogController {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @GET
    @Path("/{userId}")
    public Response getActivityLog(@PathParam("userId") long userId) {
        // Query the activity logs for the specific user
            List<NotificationEvent> events = entityManager.createQuery("SELECT a FROM NotificationEvent a WHERE a.targetUserId = :userId", NotificationEvent.class)
                .setParameter("userId", userId)
                .getResultList();

        if (events.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No activity logs found for the user").build();
        }

        return Response.ok(events).build();
    }
}

