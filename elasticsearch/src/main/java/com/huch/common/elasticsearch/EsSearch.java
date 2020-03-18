package com.huch.common.elasticsearch;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsSearch {
    public static TransportClient client = EsConfig.client;
    public static IndicesAdminClient adminClient = EsConfig.adminClient;

    @Test
    public void searchAll(){
        // 查询所有数据
        SearchResponse response = client.prepareSearch().get();
        List list = getList(response.getHits());
    }

    @Test
    public void getById(){
        // 查询所有数据
        GetRequestBuilder search = client.prepareGet("case_jgh_info", "case_jgh_info", "3b44b7d3-e7c4-4d20-9b8b-f7e01a55e994");
        GetResponse response = search.get();
        response.getSource();
        System.out.println("查询条件: " + search.toString());
        System.out.println(response.getSource());
    }

    /**
     * term查询, 精确查询, 忽略分词器, 这种查询适合keyword, numeric, date
     * 查询某个字段里含有某个关键词文档
     */
    @Test
    public void term(){
        QueryBuilder query = QueryBuilders.termQuery("ZTKID", "1399");
        SearchRequestBuilder search = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setFrom(0)
                .setSize(10)
                .setFetchSource("字段", null)     // 返回指定字段
                .setVersion(true)
                .addSort("RKSJ", SortOrder.DESC);
        search.setQuery(query);
        // 设置是否按查询匹配度排序
        search.setExplain(true);
        System.out.println("查询条件: " + search);

        SearchResponse response = search.get();
        List list = getList(response.getHits());
    }

    /**
     * terms 查询某个字段含有多个关键字文档
     */
    @Test
    public void terms(){
        // 这里多个条件是或(or)的关系
        QueryBuilder query = QueryBuilders.termsQuery("ZTKID", "1399", "838fcf8c-aec9-46cd-8c39-d3369487ade8");
//        System.out.println("查询条件: " + query);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        List list = getList(response.getHits());
    }

    /**
     * match 知道分词的存在
     * 匹配条件越多, 相似度值越高 多匹配用空格隔开
     */
    @Test
    public void match(){
        // 查询所有文档
        // QueryBuilder query = QueryBuilders.matchAllQuery();
        QueryBuilder query = QueryBuilders.matchQuery("RS16", "达拉斯");
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        List list = getList(response.getHits());
    }

    /**
     * multiMatch 多字段查询 多个字段查询同一个值
     */
    @Test
    public void multiMatch(){
        QueryBuilder query = QueryBuilders.multiMatchQuery("达拉斯", "RS16", "RS17");
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        List list = getList(response.getHits());
    }

    /**
     * 模糊查询 ?匹配单个字符 *匹配多个字符
     * 目测没有分词概念
     */
    @Test
    public void wildcardQuery(){
        QueryBuilder query = QueryBuilders.wildcardQuery("RS16", "达拉斯*");
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        List list = getList(response.getHits());
    }

    /**
     * 复合查询, 多条件查询
     */
    @Test
    public void boolQuery(){
        QueryBuilder must1 = QueryBuilders.matchQuery("RS17", "达拉斯");
        QueryBuilder must2 = QueryBuilders.matchQuery("RS14", "情报安全");
        // 复合查询构造
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(must1);
        boolQuery.must(must2);

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(boolQuery)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        List list = getList(response.getHits());
    }

    /**
     * 若文档存在则更新, 若不存在则不做处理
     */
    @Test
    public void update(){
        Map<String, Object> map = new HashMap<>();
        map.put("RS16", "奥尔巴尼分局");

        UpdateRequest request = new UpdateRequest();
        request.index("case_jgh_info").type("case_jgh_info").id("3b44b7d3-e7c4-4d20-9b8b-f7e01a55e994")
                .doc(map);
        UpdateResponse response = client.update(request).actionGet();
        System.out.println(response);
    }

    @Test
    public void update2(){
        Map<String, Object> map = new HashMap<>();
        map.put("RS16", "奥尔巴尼分局**");
        UpdateRequestBuilder update = client.prepareUpdate("case_jgh_info", "case_jgh_info", "3b44b7d3-e7c4-4d20-9b8b-f7e01a55e994");
        update.setDoc(map);
        System.out.println(update);

        UpdateResponse response = update.get();
        System.out.println(response);
    }

    /**
     * 若文档存在则更新, 若不存在则添加文档
     */
    @Test
    public void updasert(){
        Map<String, Object> map = new HashMap<>();
        map.put("RS16", "奥尔巴尼分局*");
//        IndexRequest addrequest = new IndexRequest("case_jgh_info", "case_jgh_info", "3b44b7d3-e7c4-4d20-9b8b-f7e01a55e994").source();
        UpdateRequest request = new UpdateRequest();
        request.index("case_jgh_info").type("case_jgh_info").id("3b44b7d3-e7c4-4d20-9b8b-f7e01a55e994")
                .doc(map);
//        UpdateResponse response = client.update(request).actionGet();


    }



    private List getList(SearchHits searchHits){
        System.out.println("查询总数: " + searchHits.getTotalHits() + "\t获取数量: " + searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        List list = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(i + "\t" + jsonStr);
            list.add(jsonStr);
        }
        return list;
    }



}
