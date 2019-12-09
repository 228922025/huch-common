package com.huch.common.es;

import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;

/**
 * @author huchanghua
 * @create 2019-12-09-23:09
 */
public class ElasticSearchClient {
    public static TransportClient client = EsConfig.client;
    public static IndicesAdminClient adminClient = EsConfig.adminClient;



    public ElasticSearchClient(String host, Integer port, String clustername){


    }


}
