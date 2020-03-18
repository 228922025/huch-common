package com.huch.common.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.*;

/**
 * @author huchanghua
 * @create 2019-12-10-21:46
 */
public class ESUtil {
    private static final int TIME_OUT = 5 * 60 * 1000;
    private static final String HTTP_SCHEME = "http";

    /**
     * 创建 RestClientBuilder
     * @param host
     * @param port
     * @return
     */
    public static RestClientBuilder createRestClientBuilder(String host, Integer port) {
        return RestClient.builder(new HttpHost(host, port, HTTP_SCHEME));
    }

    /**
     * 创建 RestClientBuilder
     * param address
     * @return
     */
    public static RestClientBuilder createRestClientBuilder(HttpHost... address) {
        return RestClient.builder(address);
    }

    /**
     * 创建 RestHighLevelClient 客户端
     * @param host
     * @param port
     * @return
     */
    public static RestHighLevelClient createRestHighLevelClient(String host, Integer port) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http")));
        return client;
    }

    /**
     * 创建 RestHighLevelClient 客户端
     * @param address
     * @return
     */
    public static RestHighLevelClient createRestHighLevelClient(HttpHost... address) {
        RestHighLevelClient client = new RestHighLevelClient(createRestClientBuilder(address));
        return client;
    }

    /**
     * 将查询结果解析成ListMaps
     * @param response
     * @return
     */
    public static List<Map<String, Object>> parseToListMap(SearchResponse response){
        // 从response中获得结果
        List<Map<String, Object>> list = new LinkedList<>();
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            list.add(next.getSourceAsMap());
        }
        return list;
    }

    /**
     * 将查询结果解析成ListMaps
     * @param response
     * @return
     */
    public static List<String> parseToListStr(SearchResponse response){
        // 从response中获得结果
        List<String> list = new LinkedList<>();
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            list.add(next.getSourceAsString());
        }
        return list;
    }


}
