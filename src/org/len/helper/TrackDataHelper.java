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
 * the class as a helper for keeping data in global array
 */

package org.len.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrackDataHelper {
    /* helper function for store data to global array,
    what the data? its is track number by track simulator

     */
    private static final List<String> trackData = new ArrayList<>();
    private static final Lock mutex = new ReentrantLock();

    // Set the shared data with the given value
    public static void setTrackData(String value) {
        mutex.lock();
        try {
            if (!trackData.contains(value)) {
                trackData.add(value);
            }
        } finally {
            mutex.unlock();
        }
    }

    // Get a copy of the shared data
    public static List<String> getTrackData() {
        mutex.lock();
        try {
            return new ArrayList<>(trackData);
        } finally {
            mutex.unlock();
        }
    }
}
