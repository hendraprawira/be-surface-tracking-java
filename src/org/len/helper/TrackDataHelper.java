package org.len.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrackDataHelper {
    private static List<String> data = new ArrayList<>();
    private static final Lock mutex = new ReentrantLock();

    // Set the shared data with the given value
    public static void setTrackData(String value) {
        mutex.lock();
        try {
            if (!data.contains(value)) {
                data.add(value);
            }
        } finally {
            mutex.unlock();
        }
    }

    // Get a copy of the shared data
    public static List<String> getTrackData() {
        mutex.lock();
        try {
            return new ArrayList<>(data);
        } finally {
            mutex.unlock();
        }
    }
}
