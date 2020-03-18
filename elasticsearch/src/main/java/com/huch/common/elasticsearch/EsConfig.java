package com.huch.common.elasticsearch;

import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetSocketAddress;

public class EsConfig {
    private static String host = "192.6.12.1";
    private static Integer port = 9300;
    private static String clustername = "udas";

    public static TransportClient client;
    public static IndicesAdminClient adminClient;

    static {
        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void initialize(){
        Settings settings = Settings.builder().put("cluster.name", clustername).build();
        client = TransportClient.builder()
                .settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, port)));

        adminClient = client.admin().indices();
    }
}
