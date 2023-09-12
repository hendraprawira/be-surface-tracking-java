package org.len.main;

import org.len.db.DatabaseConnector;
import org.len.db.Memcached;
import org.glassfish.tyrus.server.Server;
import org.len.kafka.KafkaConfiguration;
import org.len.websocket.Websocket;

import javax.websocket.DeploymentException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DatabaseConnector dbConn = new DatabaseConnector();
        Memcached memConn = new Memcached();
        // Kafka broker address
        String bootstrapServers = "localhost:9092";

        // Consumer group and topic
        String groupId = "yours";
        String topic = "radar";

        // Create a Kafka consumer instance

        KafkaConfiguration kafkaConsumer = new KafkaConfiguration(bootstrapServers, groupId, topic);

        // Start consuming messages in a separate thread
        kafkaConsumer.startConsumingMessagesInThread();

        Server server;
        server = new Server ("localhost", 8025, "/v1", Websocket.class);
        try {
            dbConn.connectDatabase();
            memConn.connectMemcached();
            server.start();
            System.out.println("Websocket is running");
            Websocket.storeMmc();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}