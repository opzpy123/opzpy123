package com.opzpy123.util;


import lombok.extern.slf4j.Slf4j;

import javax.management.MalformedObjectNameException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Administrator
 */
@Slf4j
public class IpAndPortUtil {

    /**
     * 获取ip地址及端口port
     *
     * @return
     * @throws MalformedObjectNameException
     * @throws NullPointerException
     * @throws UnknownHostException
     */
    public static String getIpAddressAndPort() {
        try {
//          MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
//          Set<ObjectName> objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
//                  Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
//          String port = objectNames.iterator().next().getKeyProperty("port");
            String host = InetAddress.getLocalHost().getHostAddress();
            return host + ":7001";
        } catch (Exception ex) {
            log.error("getIpAddressAndPort+EXCEPTION:" + ex);
            ex.printStackTrace();
            return "";
        }
    }
}
