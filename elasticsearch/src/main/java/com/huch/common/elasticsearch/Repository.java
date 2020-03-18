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

import java.util.HashMap;
import java.util.Map;

/**
 * 操作es的基础类
 *
 * @author huchanghua
 * @create 2019-12-28-23:04
 */
public class Repository {
    private TransportClient client;
    private IndicesAdminClient adminClient;

    public Repository(TransportClient client){
        this.client = client;
        this.adminClient = client.admin().indices();
    }

    /**
     * 全文搜索
     */
    public SearchResponse searchAll(){
        // 查询所有数据
        SearchResponse response = client.prepareSearch().get();
        return response;
    }

    /**
     * 全文搜索关键字
     * @param index
     * @param keyword
     * @return
     */
    public SearchResponse search(String index, String keyword){
        QueryBuilder query = QueryBuilders.queryStringQuery(keyword);
        SearchRequestBuilder search = client.prepareSearch(index).setTypes(index)
                .setFrom(0)
                .setSize(10)
                .setVersion(true);
        search.setQuery(query);
        // 设置是否按查询匹配度排序
        search.setExplain(true);

        SearchResponse response = search.get();
        return response;
    }

    /**
     * 根据id获取数据
     * @param index
     * @param id
     * @return
     */
    public Map<String, Object> getById(String index, String id){
        // 查询所有数据
        GetRequestBuilder search = client.prepareGet(index, index, id);
        GetResponse response = search.get();
        return response.getSource();
    }

    /**
     * term查询, 精确查询, 忽略分词器, 这种查询适合keyword, numeric, date
     * 查询某个字段里含有某个关键词文档
     */
    public SearchResponse term(String index, String field, String keyword){
        QueryBuilder query = QueryBuilders.termQuery(field, keyword);
        SearchRequestBuilder search = client.prepareSearch(index).setTypes(index)
                .setFrom(0)
                .setSize(10)
                .setVersion(true);
        search.setQuery(query);
        // 设置是否按查询匹配度排序
        search.setExplain(true);

        SearchResponse response = search.get();
        return response;
    }

    /**
     * terms 查询某个字段含有多个关键字文档, 多个关键字为或的关系
     */
    public SearchResponse terms(String index, String field, String... keyword){
        // 这里多个条件是或(or)的关系
        QueryBuilder query = QueryBuilders.termsQuery(field, keyword);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(index)
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        return response;
    }

    /**
     * match 知道分词的存在
     * 匹配条件越多, 相似度值越高 多匹配用空格隔开
     */
    public SearchResponse match(String index, String field, String keyword){
        // 查询所有文档
        // QueryBuilder query = QueryBuilders.matchAllQuery();
        QueryBuilder query = QueryBuilders.matchQuery(field, keyword);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(index)
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        return response;
    }

    /**
     * multiMatch 多字段查询 多个字段查询同一个值
     */
    public SearchResponse multiMatch(String index, String keyword, String... field){
        QueryBuilder query = QueryBuilders.multiMatchQuery(keyword, field);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(index)
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        return response;
    }

    /**
     * 模糊查询 ?匹配单个字符 *匹配多个字符
     * 目测没有分词概念
     */
    public SearchResponse wildcardQuery(String index, String field, String keyword){
        QueryBuilder query = QueryBuilders.wildcardQuery(field, keyword);
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(index)
                .setQuery(query)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        return response;
    }

    /**
     * 复合查询, 多条件查询
     */
    public SearchResponse boolQuery(String index, Map<String, Object> mustMap){
        // 复合查询构造
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 循环添加多个条件
        for (Map.Entry<String, Object> entry : mustMap.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index).setTypes(index)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(10);
        System.out.println("查询条件: " + searchRequestBuilder.toString());

        SearchResponse response = searchRequestBuilder.get();
        return response;
    }

    /**
     * 更新数据
     * 若文档存在则更新, 若不存在则不做处理
     */
    public UpdateResponse update(String index, Map<String, Object> map){
        UpdateRequest request = new UpdateRequest();
        request.index(index).type(index)
                .id(map.get("id").toString())
                .doc(map);
        UpdateResponse response = client.update(request).actionGet();
        return response;
    }

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
     * 保持文档, 文档存在更新, 不存在添加
     * 若文档存在则更新, 若不存在则添加文档
     */
    public UpdateResponse save(String index, Map<String, Object> map){
        UpdateRequest request = new UpdateRequest();
        request.index(index).type(index)
                .id(map.get("id").toString())
                .doc(map);
        UpdateResponse response = client.update(request).actionGet();
        return response;
    }


}
