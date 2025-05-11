package com.example.minisocial.NotificationsManagement;

import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.JMSException;

@MessageDriven(mappedName = "queue/NotificationQueue")  // Specify the queue you want to listen to
public class NotificationMDB implements MessageListener {

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                NotificationEvent event = (NotificationEvent) ((ObjectMessage) message).getObject();
                processEvent(event); // Call method to handle the event
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    // Handle the received event (e.g., store in DB, trigger notifications)
    private void processEvent(NotificationEvent event) {
        // Process the event, like saving it in the database or triggering another action
        System.out.println("Received event: " + event.getEventType() + " for user: " + event.getTargetUserId());
        // You can perform additional actions like sending email, updating database, etc.
    }
}
