package com.semusings.dm;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.pekko.actor.typed.ActorRef;
import org.apache.pekko.actor.typed.ActorSystem;
import org.apache.pekko.actor.typed.Props;
import org.apache.pekko.actor.typed.javadsl.Behaviors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmSenderConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory("tcp://localhost:61616", "admin", "admin");
    }

    @Bean
    public ActorSystem<Void> actorSystem() {
        return ActorSystem.create(Behaviors.empty(), "DmSystem");
    }

    @Bean
    public ActorRef<String> dmSenderActor(ConnectionFactory connectionFactory, ActorSystem<Void> system) {
        return system.systemActorOf(DmSenderActor.create(connectionFactory), "DmSenderActor", Props.empty());
    }

    @Bean
    public CommandLineRunner runner(ActorRef<String> dmSenderActor) {
        return args -> {
            dmSenderActor.tell("Hello, this is a DM!");
        };
    }
}