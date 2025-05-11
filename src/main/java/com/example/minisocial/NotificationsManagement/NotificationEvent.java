package com.example.minisocial.NotificationsManagement;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="Events")
public class NotificationEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String eventType;

    private String userId;
    private String targetUserId;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String timestamp;


    public NotificationEvent() {
    }

    public NotificationEvent(String eventType, String userId, String targetUserId, String message, String timestamp) {
        this.eventType = eventType;
        this.userId = userId;
        this.targetUserId = targetUserId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
