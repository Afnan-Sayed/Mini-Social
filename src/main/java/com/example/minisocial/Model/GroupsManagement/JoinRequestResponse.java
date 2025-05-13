package com.example.minisocial.Model.GroupsManagement;


public class JoinRequestResponse {
    public Long requestId;
    public Long userId;
    public String userName;
    public String status;

    public JoinRequestResponse() {}

    public JoinRequestResponse(Long requestId, Long userId, String userName, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

}