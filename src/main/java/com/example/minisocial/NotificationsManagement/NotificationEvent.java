package com.example.minisocial.NotificationsManagement;

import java.io.Serializable;

public class NotificationEvent implements Serializable {
    private String eventType;
    private String userId;
    private String targetUserId;
    private String message;
    private String timestamp;

    // Getters and setters for all fields

    public NotificationEvent(String eventType, String userId, String targetUserId, String message, String timestamp) {
        this.eventType = eventType;
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
