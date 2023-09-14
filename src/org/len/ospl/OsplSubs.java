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
 * the class for Handling OSPL Subscriber (receive message, and processing to websocket)
 */

package org.len.ospl;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import org.len.DotenvLoader;
import org.len.main.Main;
import org.omg.dds.core.Duration;
import org.omg.dds.core.ServiceEnvironment;
import org.omg.dds.core.policy.Durability;
import org.omg.dds.core.policy.Partition;
import org.omg.dds.core.policy.PolicyFactory;
import org.omg.dds.core.policy.Reliability;
import org.omg.dds.core.status.Status;
import org.omg.dds.domain.DomainParticipant;
import org.omg.dds.domain.DomainParticipantFactory;
import org.omg.dds.sub.Subscriber;
import org.omg.dds.sub.DataReaderQos;
import org.omg.dds.sub.DataReader;
import org.omg.dds.sub.Sample;
import org.omg.dds.topic.Topic;
import OwnPlatformData.Opf;

public class OsplSubs extends Thread {
    @Override
    public void run(){
        Map<String, String> envVariables = DotenvLoader.loadEnvVariables();
        System.setProperty(
                ServiceEnvironment.IMPLEMENTATION_CLASS_NAME_PROPERTY,
                "org.opensplice.dds.core.OsplServiceEnvironment");

        /* Instantiate a DDS ServiceEnvironment
         */
        ServiceEnvironment env = ServiceEnvironment
                .createInstance(Main.class.getClassLoader());

        DomainParticipantFactory dpf = DomainParticipantFactory
                .getInstance(env);

        /* Create Participant by domainID, same as configuration ospl.xml
         */
        DomainParticipant participant = dpf.createParticipant(1);

        /* set Reliability OSPL
         */
        Reliability reliability = PolicyFactory.getPolicyFactory(env).Reliability()
                .withReliable();

        /* set Durability OSPL
         */
        Durability durability = PolicyFactory.getPolicyFactory(env).Durability()
                .withPersistent();

        Collection<Class<? extends Status>> statuses = new HashSet<Class<? extends Status>>();

        /* set Partition
         */
        Partition partition = PolicyFactory.getPolicyFactory(env).Partition()
                .withName(envVariables.get("OSPL_PARTITION"));

        /* set Topics
         */
        Topic<Opf> topics = participant.createTopic(envVariables.get("OSPL_TOPIC"), OwnPlatformData.Opf.class,
                participant.getDefaultTopicQos().withPolicies(reliability, durability), null, statuses);

        /* Create Subscriber
         */
        Subscriber subscriber = participant.createSubscriber(participant.getDefaultSubscriberQos()
                .withPolicy(partition));

        /* set Subscriber Conf
         */
        DataReaderQos drQos = subscriber.copyFromTopicQos(subscriber.getDefaultDataReaderQos(), topics.getQos());
        DataReader<Opf> reader = subscriber.createDataReader(topics, drQos);
        Duration waitTimeout = Duration.newDuration(10, TimeUnit.SECONDS, env);


        try {
            reader.waitForHistoricalData(waitTimeout);
        } catch (TimeoutException e) {
            System.out.println("Ended");
        }
        System.out.println("OSPL Subscriber is running");

        Gson gson = new Gson();
        boolean closed = false;

        /* Receive Data from Publisher and marshal json
         */
        while (!closed) {
            Iterator<Sample<Opf>> samples = reader.take();
            while (samples.hasNext()) {
                Sample<Opf> sample = samples.next();
                System.out.println(sample);
                Opf msg = sample.getData();
                if (msg != null) {
                    String jsonArray = gson.toJson(msg);
                    System.out.println("| Received Message : "+ jsonArray + "\n");
//                    Websockets.sendMessageToAll(jsonArray);
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
                closed = true;
            }
        }

        topics.close();
        reader.close();
        subscriber.close();
        participant.close();
    }
}
