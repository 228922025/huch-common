package com.huch.common.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-12-10-21:46
 */
public class ESUtil {

    /**
     * 创建elasticsearch TransportClient 实例
     * @param host
     * @param port
     * @param clustername
     * @return
     */
    public static TransportClient createTransportClient(String host, Integer port, String clustername){
        TransportClient client = null;
        Settings settings = Settings.builder().put("cluster.name", clustername).build();
        TransportAddress transportAddress = new InetSocketTransportAddress(new InetSocketAddress(host, port));
        client = TransportClient.builder()
                .settings(settings).build()
                .addTransportAddress(transportAddress);
        return client;
    }

    /**
     * 解析查询数据到List<Map<String, Object>
     * @param response
     * @return
     */
    public static List<Map<String, Object>> parseToListMap(SearchResponse response){
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List list = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            Map map = JSON.parseObject(jsonStr, Map.class);
            map.put("_id", _id);
            list.add(map);
        }
        return list;
    }

    /**
     * 解析搜索结果到List String
     * @param response
     * @return
     */
    public static List<String> parseToListStr(SearchResponse response){
        SearchHits searchHits = response.getHits();
        SearchHit[] hits = searchHits.getHits();
        List list = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            String str = hit.getSourceAsString();
            list.add(str);
        }
        return list;

    }


}
