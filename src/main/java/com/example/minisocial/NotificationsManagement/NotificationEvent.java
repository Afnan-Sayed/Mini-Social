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
    @Column(nullable = true)
    private Long senderUserId;
    private long targetUserId;

    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String timestamp;


    public NotificationEvent() {
    }

    public NotificationEvent(String eventType, Long senderUserId, long targetUserId, String message, String timestamp) {
        this.eventType = eventType;
        this.senderUserId = senderUserId;
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

    public long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(long userId) {
        this.senderUserId = userId;
    }

    public long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(long targetUserId) {
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
