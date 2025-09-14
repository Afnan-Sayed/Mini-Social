package com.example.minisocial.NotificationsManagement;


import jakarta.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class NotificationProducer {

    // The JNDI name of the JMS queue
    private static final String QUEUE_NAME = "queue/MyTrelloQueue";  // JMS queue JNDI name

    public void sendNotification(NotificationEvent event) {
        try {
            // Create JNDI context and lookup the connection factory and queue
            InitialContext context = new InitialContext();
            QueueConnectionFactory connectionFactory = (QueueConnectionFactory) context.lookup("java:/ConnectionFactory");
            QueueConnection connection = connectionFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = (Queue) context.lookup(QUEUE_NAME);  // Look up the JMS queue by its JNDI name

            // Create a JMS sender and message
            QueueSender sender = session.createSender(queue);

            // Create an ObjectMessage to hold the NotificationEvent object
            ObjectMessage message = session.createObjectMessage();
            message.setObject(event);  // Set the event object as the JMS message payload

            // Send the message to the queue
            sender.send(message);

            System.out.println("Notification message sent: " + event.getMessage());

            // Close the connection
            connection.close();

        } catch (JMSException | NamingException e) {
            e.printStackTrace();
        }
    }
}
