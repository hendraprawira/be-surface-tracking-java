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
 * the class for Handling consumer kafka for receive message from track simulator
 */

package org.len.kafka;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.len.helper.TrackDataHelper;
import org.len.model.TrackSim;
import org.len.websocket.Websocket;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Future;

import static org.len.db.Memcached.memcacheClient;

public class KafkaConfiguration {
    private final KafkaConsumer<String, String> consumer;
    private volatile boolean running = true;

    /* setup conf for connect to kafka as consumer
    */
    public KafkaConfiguration(String bootstrapServers, String groupId, String topic) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        this.consumer = new KafkaConsumer<>(properties);
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaConfiguration(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    /* consuming messages from kafka marshal to TrackSim model class,
        store to memcached client
        push to websocket
        set TrachNumber to global array
     */
    public void startConsumingMessagesInThread() {
        ObjectMapper objectMapper = new ObjectMapper();
        Thread consumerThread = new Thread(() -> {
            while (running) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1));
                for (ConsumerRecord<String, String> record : records) {
                    TrackSim trackSim = null; // Initialize the Tracking object
                    try {
                        trackSim = objectMapper.readValue( record.value(), TrackSim.class);
                        System.out.printf("Received message: value = %s%n", trackSim);
                        memcacheClient.set(String.valueOf(trackSim.getTrackNumber()), 0, record.value());
                        Websocket.sendMessageToAll(record.value());
                        TrackDataHelper.setTrackData(String.valueOf(trackSim.getTrackNumber()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle the exception as needed
                    }
                }
            }
            consumer.close();
        });
        consumerThread.start();

    }

    public void stop() {
        running = false;
    }
}
