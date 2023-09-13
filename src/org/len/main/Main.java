/*
 * Copyright PT Len Industri (Persero)
 *
 * THIS SOFTWARE SOURCE CODE AND ANY EXECUTABLE DERIVED THEREOF ARE PROPRIETARY
 * TO PT LEN INDUSTRI (PERSERO), AS APPLICABLE, AND SHALL NOT BE USED IN ANY WAY
 * OTHER THAN BEFOREHAND AGREED ON BY PT LEN INDUSTRI (PERSERO), NOR BE REPRODUCED
 * OR DISCLOSED TO THIRD PARTIES WITHOUT PRIOR WRITTEN AUTHORIZATION BY
 * PT LEN INDUSTRI (PERSERO), AS APPLICABLE.
 */

/*
 * @author Hendra
 * */

/*
 * Main Class Surface Tracking
 */


package org.len.main;

import org.len.DotenvLoader;
import org.len.db.DatabaseConnector;
import org.len.db.Memcached;
import org.glassfish.tyrus.server.Server;
import org.len.kafka.KafkaConfiguration;
import org.len.ospl.OsplSubs;
import org.len.websocket.Websocket;

import javax.websocket.DeploymentException;
import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        /* start OSPL thread,
        */
        OsplSubs osplSubs = new OsplSubs();
        osplSubs.start();

        /* load env var,
         */
        Map<String, String> envVariables = DotenvLoader.loadEnvVariables();

        /* Create a Kafka consumer instance
         */
        KafkaConfiguration kafkaConsumer = new KafkaConfiguration(envVariables.get("KAFKA_HOST"), envVariables.get("KAFKA_GROUP_ID"), envVariables.get("TOPIC_BROKER"));

        // Start consuming messages in a separate thread
        kafkaConsumer.startConsumingMessagesInThread();

        /* create server for websocket
         */
        Server server;
        server = new Server ("localhost", Integer.parseInt(envVariables.get("ACTIVE_PORT")), "/v1", Websocket.class);
        try {
            /* db connect, memcached connect, start server and start websocket store data to memcached
             */
            DatabaseConnector.connectDatabase();
            Memcached.connectMemcached();
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