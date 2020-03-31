package com.huch.common.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * 操作es的基础类
 *
 * @author huchanghua
 * @create 2019-12-28-23:04
 */
public class Repository {
    private static Logger logger = LoggerFactory.getLogger(Repository.class);
    private RestHighLevelClient client;

    public Repository(RestHighLevelClient restHighLevelClient) {
        this.client = restHighLevelClient;
    }

    /**
     * 获取estClient
     * getRestHighLevelClient
     *
     * @return
     */
    public RestHighLevelClient getRestHighLevelClient() {
        return this.client;
    }

    /**
     * 创建索引
     * @param jsonstr
     * @return
     */
    public boolean createIndex(String jsonstr){
        JSONObject json = JSON.parseObject(jsonstr);
        //CreateIndexRequest 实例， 需要注意包的版本 我这里用的7.2的版本 org.elasticsearch.client.indices;
        CreateIndexRequest request = new CreateIndexRequest(json.getString("index"));
        //设置分片
        request.settings(Settings.builder()
                .put("index.number_of_shards", json.getString("number_of_shards"))
                .put("index.number_of_replicas", json.getString("number_of_replicas"))
        );
        // 方式一
        request.mapping(json.getString("mapping"), XContentType.JSON);
        //我使用的同步的方式 异步请参考官方文档
        CreateIndexResponse createIndexResponse = null;
        try {
            createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new Exception("创建索引失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 创建一个空的索引
     *
     * @param index
     * @param shards
     * @param replicas
     * @return
     */
    public boolean createIndex(String index, Integer shards, Integer replicas) {
        CreateIndexRequest request = new CreateIndexRequest(index);
        request.settings(Settings.builder()
                .put("index.number_of_shards", shards)   //分片数
                .put("index.number_of_replicas", replicas));//副本数
        // request.alias(new Alias(index + "alias"));//设置别名
        request.setTimeout(TimeValue.timeValueMinutes(2));//设置创建索引超时2分钟
        // 同步请求(亲测可以)
        try {
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            // 处理响应
            boolean acknowledged = createIndexResponse.isAcknowledged();
            boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
            System.out.println(acknowledged + "," + shardsAcknowledged);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("索引{}创建异常:" + e.getMessage(), index);
            return false;
        }
        return true;
    }


    /**
     * 判断索引是否存在
     *
     * @param index
     * @return
     */
    public boolean indexExists(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            boolean bool = client.indices().exists(request, RequestOptions.DEFAULT);
            return bool;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除索引
     *
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);//指定要删除的索引名称
        //可选参数：
        //设置超时，等待所有节点确认索引删除（使用TimeValue形式）
        request.timeout(TimeValue.timeValueMinutes(2));
        // 设置超时，等待所有节点确认索引删除（使用字符串形式）
        // request.timeout("2m");

        //连接master节点的超时时间(使用TimeValue方式)
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        //连接master节点的超时时间(使用字符串方式)
        // request.masterNodeTimeout("1m");

        //设置IndicesOptions控制如何解决不可用的索引以及如何扩展通配符表达式
        request.indicesOptions(IndicesOptions.lenientExpandOpen());

        //同步执行
        AcknowledgedResponse delete = client.indices().delete(request, RequestOptions.DEFAULT);
        if (delete.isAcknowledged()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建mapping
     * 注意数据格式，此版本已经取去除String格式，改为text和keyword格式，其中text格式支持分词和建立索引，
     * 支持模糊查询和精确查询，不支持聚合，keyword不支持分词，支持模糊查询和精确查询，支持聚合查询，排序
     * @param index
     * @return
     */
    public String createMapping(String index) {
        String result = "mapping创建成功";
        PutMappingRequest putMappingRequest = new PutMappingRequest(index);

        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("properties")
                    .startObject("id")
                    .field("type", "keyword")
                    .field("index", true)
                    .endObject()
                    .startObject("pics")
                    .field("type", "text")
                    .field("index", false)
                    .endObject()
                    .startObject("name")
                    .field("type", "text")
                    .field("index", true)
                    //分词器采用ik_smart分词器
                    .field("analyzer", "ik_smart")
                    .endObject()
                    .startObject("prices")
                    .field("type", "double")
                    .field("index", true)
                    .endObject()
                    //可以按照城市排序，需要在其中再套一层，并且格式为keyword
                    .startObject("city")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("raw")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    //支持指定时间格式
                    .startObject("createTime")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        putMappingRequest.source(builder);
        try {
            AcknowledgedResponse putMappingResponse = client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
            System.out.println(putMappingResponse);
            if (!putMappingResponse.isAcknowledged()) {
                result = "接口执行失败";
            } else {
                result = "mapping“”已经存在";
            }
        } catch (IOException e) {
            result = "mapping创建接口异常";
        }
        return result;
    }


    /**
     * 保存数据-同步
     * @param index 索引
     * @param map 数据
     * @throws Exception
     */
    public void save(String index, Map<String, Object> map) throws Exception {
        IndexRequest request = new IndexRequest(index);
        request.id(map.get("id").toString());    //ID也可使用内部自动生成的 不过希望和数据库统一唯一业务ID
        request.source(JSON.toJSONString(map), XContentType.JSON);
        // 同步执行
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    }

    /**
     * 保存数据-异步
     * @param index 索引
     * @param map 数据
     */
    public void saveAsync(String index, Map<String, Object> map){
        IndexRequest request = new IndexRequest(index);
        request.id(map.get("id").toString());    //ID也可使用内部自动生成的 不过希望和数据库统一唯一业务ID
        request.source(JSON.toJSONString(map), XContentType.JSON);

        //异步方法不会阻塞并立即返回。
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                //执行成功时调用。 Response以参数方式提供
            }

            @Override
            public void onFailure(Exception e) {
                //在失败的情况下调用。 引发的异常以参数方式提供

            }
        };
        //异步执行索引请求需要将IndexRequest实例和ActionListener实例传递给异步方法：
        Cancellable cancellable = client.indexAsync(request, RequestOptions.DEFAULT, listener);
        System.out.println(cancellable);
    }


    /**
     * 根据id获取数据
     * @param index
     * @param id
     * @return
     * @throws IOException
     */
    public Map<String, Object> getById(String index, String id) throws IOException {
        GetResponse response = client.get(new GetRequest(index, id), RequestOptions.DEFAULT);
        return response.getSource();
    }

    /**
     * 搜索数据
     * @param index
     * @param keyword
     * @return
     * @throws IOException
     */
    public SearchResponse search(String index, String keyword) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keyword);

        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return response;
    }


    /**
     * 多条件查询 and关系
     * @param mustMap
     * @param length
     * @return
     */
    public SearchResponse multiSearch(String index, Map<String, Object> mustMap, int length) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        // 根据多个条件 生成 boolQueryBuilder
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        // 循环添加多个条件
        for (Map.Entry<String, Object> entry : mustMap.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(entry.getKey(), entry.getValue()));
        }

        // boolQueryBuilder生效
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.size(length);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return response;
    }
}
