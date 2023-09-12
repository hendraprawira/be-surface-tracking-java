package org.len.websocket;
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
 * the class for Websocket,
 */

import org.len.helper.TrackDataHelper;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.ServerEndpoint;

import static org.len.db.Memcached.mc;

@ServerEndpoint(value = "/ownplatform")
public class Websocket {
    private static List<Session> sessions = new ArrayList<>();
    @OnOpen
    public void onOpen(Session session) {
        System.out.println ("Connected, sessionID = " + session.getId());
        sessions.add(session);
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        System.out.println(message);
        if (message.equals("quit")) {
            try {
                session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Bye!"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Session " + session.getId() +
                " closed because " + closeReason);
    }

    // Create a function to send messages to all connected clients
    public static void sendMessageToAll(String message) {

            Thread senderThread = new Thread(() -> {
                if (sessions != null){
                for (Session session : sessions) {
                    try {
                        session.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        // Handle exceptions, such as closed sessions
                        e.printStackTrace();
                        System.out.println("meuni hemm");
                    }
                }
                }
            });
            senderThread.start();

    }

    public static void storeMmc() {
        int index = 0;
        while(true){
            if(!TrackDataHelper.getTrackData().isEmpty()) {
                try {
                    int finalIndex = index;
                    Thread mmcThread = new Thread(() -> {

                        Map<String, Object> result = mc.getBulk(TrackDataHelper.getTrackData());
                        for (String key : TrackDataHelper.getTrackData()) {
                            Object value = result.get(key);
                            if (value != null) {
                                // The value is found in Memcached
                                String keyVal = "log-" + finalIndex + "-track-" + key;
                                mc.set(keyVal, 0, value);
                                System.out.println("Value for key '" + keyVal + "': " + value.toString());
                            } else {
                                // The value is not found in Memcached
                                System.out.println("Key '" + key + "' not found in Memcached.");
                            }
                        }

                    });
                    mmcThread.start();

                    // Sleep for 10 seconds (10000 milliseconds)
                    Thread.sleep(10000);
                    System.out.println("After sleep");
                    if (index == 99) {
                        index = 0;
                    } else {
                        index++;
                    }
                } catch (InterruptedException e) {
                    // Handle the InterruptedException if necessary
                    e.printStackTrace();
                }
            }
        }


    }
}
