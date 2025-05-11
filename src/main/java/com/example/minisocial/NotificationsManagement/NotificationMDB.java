package com.example.minisocial.NotificationsManagement;

import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.JMSException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@MessageDriven(mappedName = "queue/NotificationQueue")
public class NotificationMDB implements MessageListener {

    @PersistenceContext(unitName = "myPersistenceUnit")
    private EntityManager entityManager;

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage) {
            try {
                NotificationEvent event = (NotificationEvent) ((ObjectMessage) message).getObject();
                processEvent(event);  // Process the event
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private void processEvent(NotificationEvent event) {
        // Save the event to the database
        entityManager.persist(event);  // Store the notification in the database
    }
}
