package com.example.minisocial.Controller.GroupsManagement;

import com.example.minisocial.Model.GroupsManagement.*;
import com.example.minisocial.Model.PostManagement.Post.Post;
import com.example.minisocial.Service.GroupsManagement.GroupFacade;

import com.example.minisocial.Service.GroupsManagement.GroupService;
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
            Group group = groupFacade.getGroup(groupId);  // fetch the group to check if it's open or not
            groupFacade.joinGroup(userId, groupId);
            String userName = groupFacade.getUserName(userId);

            if (group.isOpen()) {
                return Response.ok("{\"message\": \"" + userName + " joined the group\"}").build();
            } else {
                return Response.ok("{\"message\": \"" + userName + "'s request is pending approval\"}").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
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

    @DELETE
    @Path("/{groupId}")
    public Response deleteGroup(@PathParam("groupId") Long groupId, @QueryParam("adminId") Long adminId) {
        try {
            groupFacade.deleteGroup(groupId, adminId);
            return Response.ok("{\"message\": \"Group deleted successfully\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

   /* @GET
    @Path("/{groupId}/requests")
    public Response getPendingRequests(@PathParam("groupId") Long groupId,
                                       @QueryParam("adminId") Long adminId) {
        try {
            List<JoinRequest> requests = groupFacade.getPendingJoinRequests(groupId);
            List<JoinRequestResponse> response = new ArrayList<>();

            for (JoinRequest req : requests) {
                JoinRequestResponse dto = new JoinRequestResponse();
                dto.requestId = req.getId();
                dto.userId = req.getUser().getId();
                dto.userName = req.getUser().getName();
                dto.status = req.getStatus().name();
                response.add(dto);
            }

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }
    */
    ///
    @GET
    @Path("/{groupId}/pending-requests")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPendingJoinRequests(@PathParam("groupId") Long groupId) {
        try {
            List<JoinRequest> pendingRequests = groupFacade.getPendingJoinRequests(groupId);
            return Response.ok(pendingRequests).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    ///post
@GET
@Path("/{groupId}/posts")
public Response getGroupPosts(@PathParam("groupId") Long groupId,
                              @QueryParam("userId") Long userId) {
    try {
        List<GroupPostResp> posts = groupFacade.getGroupPostsForMember(groupId, userId);
        return Response.ok(posts).build();
    } catch (SecurityException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
    } catch (Exception e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
    }
}

    @DELETE
    @Path("/{groupId}/posts/{postId}/remove")
    public Response removePost(@PathParam("groupId") Long groupId,
                               @PathParam("postId") Long postId,
                               @QueryParam("adminId") Long adminId) {
        try {
            groupFacade.removePostFromGroup(groupId, adminId, postId);
            return Response.ok("{\"message\": \"Post removed successfully\"}").build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/requests/{requestId}/handle")
    public Response handleJoinRequest(@PathParam("requestId") Long requestId,
                                      @QueryParam("adminId") Long adminId,
                                      @QueryParam("approve") boolean approve) {
        try {
            groupFacade.handleJoinRequest(requestId, adminId, approve);
            return Response.ok("{\"message\": \"Request processed.\"}").build();
        } catch (SecurityException e) {
            return Response.status(Response.Status.FORBIDDEN).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }


}


