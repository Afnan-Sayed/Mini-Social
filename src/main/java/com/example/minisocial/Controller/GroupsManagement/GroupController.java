package com.example.minisocial.Controller.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.CreateGroupRequest;
import com.example.minisocial.Model.GroupsManagement.Group;
import com.example.minisocial.Model.GroupsManagement.GroupResponse;
import com.example.minisocial.Service.GroupsManagement.GroupFacade;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GroupController {

    @Inject
    private GroupFacade groupFacade;

    @POST
    @Path("/create")
    public Response createGroup(CreateGroupRequest request) {
        try {
            if (request.creatorId == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Missing creatorId\"}")
                        .build();
            }

            groupFacade.createGroup(request.name, request.description, request.isOpen, request.creatorId);
            return Response.status(Response.Status.CREATED).entity("{\"message\": \"Group created\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/{groupId}/join")
    public Response joinGroup(@PathParam("groupId") Long groupId, @QueryParam("userId") Long userId) {
        try {
            groupFacade.joinGroup(userId, groupId);
            String userName = groupFacade.getUserName(userId);
            return Response.ok("{\"message\": \"" + userName + " joined the group\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{groupId}")
    public Response getGroup(@PathParam("groupId") Long groupId) {
        Group group = groupFacade.getGroup(groupId);
        if (group == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\": \"Group not found\"}").build();
        }

        GroupResponse dto = new GroupResponse();
        dto.id = group.getId();
        dto.name = group.getName();
        dto.description = group.getDescription();
        dto.isOpen = group.isOpen();
        if (group.getCreator() != null) {
            dto.creatorId = group.getCreator().getId();
            dto.creatorName = group.getCreator().getName();
        }
        List<String> names = new ArrayList<>();
        for (var user : group.getMembers()) {
            names.add(user.getName());
        }
        dto.memberNames = names;

        return Response.ok(dto).build();
    }
    @POST
    @Path("/{groupId}/promote")
    public Response promoteUserToAdmin(@PathParam("groupId") Long groupId,
                                       @QueryParam("adminId") Long adminId,
                                       @QueryParam("userId") Long userId) {
        try {
            groupFacade.promoteToAdmin(groupId, adminId, userId);
            String userName = groupFacade.getUserName(userId);
            return Response.ok("{\"message\": \"" + userName + " has been promoted to admin\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{groupId}/remove")
    public Response removeUserFromGroup(@PathParam("groupId") Long groupId,
                                        @QueryParam("adminId") Long adminId,
                                        @QueryParam("userId") Long userId) {
        try {
            String removedUserName = groupFacade.removeUserFromGroup(groupId, adminId, userId);
            return Response.ok("{\"message\": \"" + removedUserName + " has been removed from the group\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }


}
