package com.semusings.dm;

import jakarta.jms.*;
import org.apache.pekko.actor.typed.Behavior;
import org.apache.pekko.actor.typed.javadsl.Behaviors;

public class DmSenderActor {

    public static Behavior<String> create(ConnectionFactory connectionFactory) {
        return Behaviors.setup(context -> Behaviors.receiveMessage(dmContent -> {
            try (Connection connection = connectionFactory.createConnection()) {
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                Queue queue = session.createQueue("dm-queue");
                MessageProducer producer = session.createProducer(queue);
                TextMessage message = session.createTextMessage(dmContent);
                producer.send(message);
                producer.close();
                session.close();
            } catch (Exception e) {
                context.getLog().error("Failed to send DM", e);
            }
            return Behaviors.same();
        }));
    }

}