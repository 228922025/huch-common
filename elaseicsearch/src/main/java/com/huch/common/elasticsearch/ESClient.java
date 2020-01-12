package com.huch.common.elasticsearch;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-12-10-21:57
 */
public class ESClient {

    private TransportClient client;
    private IndicesAdminClient adminClient;

    private ESClient(){}

    public ESClient(TransportClient client) {
        this.client = client;
        this.adminClient = client.admin().indices();
    }

    public ESClient(String host, Integer port, String clustername){
        Settings settings = Settings.builder().put("cluster.name", clustername).build();
        client = TransportClient.builder()
                .settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, port)));

        adminClient = client.admin().indices();
    }


    public void insert(String indexName, String type, Map<String, Object> data) {
        String id = data.get("id").toString();

        IndexResponse response = client.prepareIndex(indexName, type, "1")
                .setSource(data).get();
    }

    /**
     * 查询所有数据
     * @return
     */
    public List<Map<String, Object>> searchAll(){
        // 查询所有数据
        SearchResponse response = client.prepareSearch().get();
        return ESUtil.parseToMap(response);
    }


    /**
     * 根据id查询数据
     */
    public Map<String, Object> getById(String indexName, String type, String id){
        // 查询所有数据
        GetRequestBuilder search = client.prepareGet(indexName, type, id);
        GetResponse response = search.get();
        response.getSource();
        return response.getSource();
    }

    /**
     * term查询, 精确查询, 忽略分词器, 这种查询适合keyword, numeric, date
     * 查询某个字段里含有某个关键词文档
     */
    public List<Map<String, Object>> term(String indexName, String type, String field, String value) {
        QueryBuilder query = QueryBuilders.termQuery(field, value);
        SearchRequestBuilder search = client.prepareSearch(indexName).setTypes(type);
                // .setFrom(0)
                // .setSize(10)
                // .setFetchSource("字段", null)     // 返回指定字段
                // .setVersion(true)
                // .addSort("RKSJ", SortOrder.DESC);
        search.setQuery(query);
        // 设置是否按查询匹配度排序
        search.setExplain(true);
        SearchResponse response = search.get();
        return ESUtil.parseToMap(response);

    }

}
