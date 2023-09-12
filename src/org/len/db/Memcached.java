package org.len.db;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.AddrUtil;
import org.len.DotenvLoader;

import java.util.Map;

public class Memcached {
    // Global MemcachedClient instance
    public static MemcachedClient mc;
    // Establish a connection to Memcached
    public static void connectMemcached() {
        Map<String, String> envVariables = DotenvLoader.loadEnvVariables();
        String memCacheHost = envVariables.get("MEMCACHED_HOST");
        try {
            mc = new MemcachedClient(AddrUtil.getAddresses(memCacheHost));
            System.out.println("Memcached Connected");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
}
