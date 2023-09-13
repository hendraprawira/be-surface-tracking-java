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
 * the class for memcached driver
 */

package org.len.db;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.AddrUtil;
import org.len.DotenvLoader;

import java.util.Map;

public class Memcached {
    // Global MemcachedClient instance
    public static MemcachedClient memcacheClient;
    // Establish a connection to Memcached
    public static void connectMemcached() {
        /* make memchaced connection with var host by env file

         */
        Map<String, String> envVariables = DotenvLoader.loadEnvVariables();
        String memCacheHost = envVariables.get("MEMCACHED_HOST");
        try {
            memcacheClient = new MemcachedClient(AddrUtil.getAddresses(memCacheHost));
            System.out.println("Memcached Connected");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
