package com.huch.common.es;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther huch
 * @Date 2019/7/10 16:45
 */
public class EsTest {

    private final static String HOST = "192.6.12.1";
    private final static int PORT = 9300;
    private final static String CLUSTERNAME = "udas";
    private TransportClient client = null;
    private IndicesAdminClient adminClient = null;

    /*static {
        PropertiesUtil.getCommonProperties().getProperty("ES_IP");
        Integer.parseInt(PropertiesUtil.getCommonProperties().getProperty("ES_PORT"));
        PropertiesUtil.getCommonProperties().getProperty("ES_CLUSTERNAME");
    }*/

    public EsTest(){
        getClient(HOST, PORT, CLUSTERNAME);
        getAdminClient();
    }

    private TransportClient getClient(String host, int port, String clusterName) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        this.client = TransportClient.builder()
                .settings(settings).build()
                .addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(host, port)));
        return this.client;
    }

    private IndicesAdminClient getAdminClient(){
        this.adminClient = client.admin().indices();
        return this.adminClient;
    }

    @Test
    public void esInfoTest(){
        EsTest es = new EsTest();

        List<DiscoveryNode> nodes = client.connectedNodes();
        for (DiscoveryNode node : nodes) {
            System.out.println(node.getHostAddress());
        }
    }

    @Test
    public void createJsonTest() throws IOException {
        EsTest es = new EsTest();
        XContentBuilder source = XContentFactory.jsonBuilder()
                .startObject()
                .field("user", "kimchy")
                .field("postDate", new Date())
                .field("message", "trying to Elasticsearch")
                .endObject();
        System.out.println(source.string());
        System.out.println(JSONObject.toJSONString(source));
    }

    @Test
    public void insertTest() throws IOException {
        EsTest es = new EsTest();
        XContentBuilder source = XContentFactory.jsonBuilder()
                .startObject()
                .field("RS1", "华盛顿总部")
                .field("RS2", "38")
                .field("RS3", "-77")
                .field("RS4", "(410) 265-8080")
                .field("RS5", "2600 Lord Baltimore Drive Baltimore, MD 21244 ")
                .field("RS6", "Covers the entire states of Maryland and Delaware")
                .field("RS7", "baltimore.fbi.gov")
                .field("RS8", "1")
                .field("ZTKID", "1399")
                .endObject();

        String json = "";

        IndexResponse response = es.client.prepareIndex("case_jgh_info", "case_jgh_info", "1").setSource(source).get();
        System.out.println(response);
    }


    @Test
    public void searchTest(){
        EsTest es = new EsTest();
        QueryBuilder query = QueryBuilders.termQuery("ZTKID", "189df106-633e-4fda-b914-57d969920824");
        SearchResponse response = es.client.prepareSearch("case_jgh_info")
                .setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();

        // 分析结果集
        SearchHits searchHits = response.getHits();
        System.out.println("查询总数: " + searchHits.getTotalHits());

        SearchHit[] hits = searchHits.getHits();
        List hitList = new ArrayList();
        for(SearchHit hit: hits){
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(jsonStr);
        }
    }

    @Test
    public void updateTest(){
        EsTest es = new EsTest();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.multiMatchQuery("1399", "ZTKID").operator(MatchQueryBuilder.Operator.AND));
        query.must(QueryBuilders.multiMatchQuery("Honolulu", "RS1").operator(MatchQueryBuilder.Operator.AND));
//        QueryBuilder query = QueryBuilders.termQuery("ZTKID", "189df106-633e-4fda-b914-57d969920824");
        SearchResponse response = es.client.prepareSearch("case_jgh_info")
                .setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10000)
                .execute().actionGet();
        // 分析结果集
        SearchHits searchHits = response.getHits();
        System.out.println("查询总数: " + searchHits.getTotalHits());

        SearchHit[] hits = searchHits.getHits();
        List hitList = new ArrayList();
        for(SearchHit hit: hits){
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(jsonStr);

            // 更新数据
            Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
//            String name = map.get("RS2").toString().trim() + " " + map.get("RS10").toString().trim() + ". " + map.get("RS8").toString().trim();
//            map.put("RS2", name);

            map.put("RS2", "21.3174224");
            map.put("RS3", "-158.068521");
            map.put("RS12", "1");
            map.put("RS8", "2");
            es.client.prepareIndex("case_jgh_info", "case_jgh_info", _id).setSource(map).get();

        }
    }

    @Test
    public void updateNameTest(){
        EsTest es = new EsTest();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.multiMatchQuery("189df106-633e-4fda-b914-57d969920824", "ZTKID").operator(MatchQueryBuilder.Operator.AND));
        query.must(QueryBuilders.multiMatchQuery("A16C095464F23152", "PC").operator(MatchQueryBuilder.Operator.AND));
        SearchResponse response = es.client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query).setScroll(new TimeValue(1000))
                .setFrom(0)
                .setSize(10000)
                .execute().actionGet();

        // 分析结果集
        SearchHits hits1 = response.getHits();
        System.out.println("查询总数: " + hits1.getTotalHits() + "\t获取数量: " + hits1.getTotalHits());

        response = es.client.prepareSearchScroll(response.getScrollId()).setScroll(new Scroll(new TimeValue(20000))).execute().actionGet();
        // 分析结果集
        SearchHits hits2 = response.getHits();
        System.out.println("查询总数: " + hits2.getTotalHits());

        response = es.client.prepareSearchScroll(response.getScrollId()).setScroll(new Scroll(new TimeValue(20000))).execute().actionGet();
        // 分析结果集
        SearchHits hits3 = response.getHits();
        System.out.println("查询总数: " + hits3.getTotalHits());

//        response = es.client.prepareSearch("case_jgh_info")
//                .setTypes("case_jgh_info")
//                .setQuery(query)
//                .setFrom(20000)
//                .setSize(10000)
//                .execute().actionGet();
//        // 分析结果集
//        SearchHits hits3 = response.getHits();
//        System.out.println("查询总数: " + hits3.getTotalHits());

        SearchHit[] hits = hits1.getHits();
        List hitList = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(i + "\t" + jsonStr);

            // 更新数据
//            Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
//            String name = map.get("RS2").toString().trim() + " " + map.get("RS10").toString().trim() + ". " + map.get("RS8").toString().trim();
//            map.put("RS2", name);
//            es.client.prepareIndex("case_jgh_info", "case_jgh_info", _id).setSource(map).get();

        }

        SearchHit[] h2 = hits2.getHits();
        for (int i = 0; i < h2.length; i++) {
            SearchHit hit = h2[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(i + "\t" + jsonStr);

        }

        SearchHit[] h3 = hits3.getHits();
        for (int i = 0; i < h3.length; i++) {
            SearchHit hit = h3[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(i + "\t" + jsonStr);

        }
    }

    @Test
    public void updateTest3(){
        EsTest es = new EsTest();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.multiMatchQuery("189df106-633e-4fda-b914-57d969920824", "ZTKID").operator(MatchQueryBuilder.Operator.AND));
        query.must(QueryBuilders.multiMatchQuery("A16C2D52ADD46000", "PC").operator(MatchQueryBuilder.Operator.AND));
        SearchResponse response = es.client.prepareSearch("case_jgh_info").setTypes("case_jgh_info")
                .setQuery(query).setScroll(new TimeValue(1000))
                .setFrom(0)
                .setSize(10000)
                .execute().actionGet();

        // 分析结果集
        SearchHits hits1 = response.getHits();
        System.out.println("查询总数: " + hits1.getTotalHits() + "\t获取数量: " + hits1.getTotalHits());

        SearchHit[] hits = hits1.getHits();
        List hitList = new ArrayList();
        for (int i = 0; i < hits.length; i++) {
            SearchHit hit = hits[i];
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
//            System.out.println(i + "\t" + jsonStr);

            // 更新数据
            Map<String, Object> map = JSONObject.parseObject(jsonStr, Map.class);
            if(map.get("RS20").toString().equals("")){
                map.put("RS20", "浙江厅");
                map.put("RS21", "20190718");
                es.client.prepareIndex("case_jgh_info", "case_jgh_info", _id).setSource(map).get();
            }
        }
    }

    @Test
    public void searchTest2(){
        EsTest es = new EsTest();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.multiMatchQuery("189df106-633e-4fda-b914-57d969920824", "ZTKID").operator(MatchQueryBuilder.Operator.AND));
        query.must(QueryBuilders.multiMatchQuery("Washington", "RS3").operator(MatchQueryBuilder.Operator.AND));
        query.must(QueryBuilders.multiMatchQuery("tim pappa", "RS2").operator(MatchQueryBuilder.Operator.AND));

//        query.must(QueryBuilders.multiMatchQuery("Washington", "RS1").operator(MatchQueryBuilder.Operator.AND));
//        query.must(QueryBuilders.rangeQuery("RKSJ").gt("2019-07-18").lt("2019-07-20"));
        System.out.println(query.toString());

        SearchResponse response = es.client.prepareSearch("case_jgh_info")
                .setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(10)
                .execute().actionGet();

        SearchHits hits = response.getHits();
        System.out.println("查询总数: " + hits.getTotalHits());

        for(SearchHit hit: hits){
            String _id = hit.getId();
            String jsonStr = hit.getSourceAsString();
            System.out.println(jsonStr);
        }

    }

    @Test
    public void exportExcel(){
//        ApplicationContext context = new ClassPathXmlApplicationContext("datasource-servlet.xml");
//        JdbcTemplate pzkJdbcTemplate = (JdbcTemplate) SpringContextUtil.getBean("pzkJdbcTemplate");
//        System.out.println(pzkJdbcTemplate);
//        EsTest es = new EsTest();
//        BoolQueryBuilder query = QueryBuilders.boolQuery();
//        query.must(QueryBuilders.matchQuery("ZTKID", "1399").operator(MatchQueryBuilder.Operator.AND));
//        query.must(QueryBuilders.matchQuery("RS8", "2").operator(MatchQueryBuilder.Operator.AND));
////        query.must(QueryBuilders.matchQuery("RS12", "1").operator(MatchQueryBuilder.Operator.AND));
//
//        SearchResponse response = es.client.prepareSearch("case_jgh_info")
//                .setTypes("case_jgh_info")
//                .setQuery(query)
//                .setFrom(0)
//                .setSize(1000)
//                .execute().actionGet();
//
//        SearchHits hits = response.getHits();
//        System.out.println("查询总数: " + hits.getTotalHits());
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for(SearchHit hit: hits){
//            String _id = hit.getId();
//            String jsonStr = hit.getSourceAsString();
//            System.out.println(jsonStr);
//            Map map = JSONObject.parseObject(jsonStr, Map.class);
//            Set set = map.entrySet();
//            Iterator iterator = set.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
//                map.put(entry.getKey(), "\"" + entry.getValue() + "\"");
//            }
//            list.add(map);
//        }
//        String sql = "select * from case_col_mapping t where ztkid = ? order by xh";
//        List<Map<String, Object>> cols = pzkJdbcTemplate.queryForList(sql, "1399");
//        LinkedHashMap<String, String> head = new LinkedHashMap<>();
//        for(int i = 0; i < cols.size(); i++){
//            Map<String, Object> m = cols.get(i);
//            head.put(m.get("COLNAME").toString(), m.get("COLNAME").toString());
//        }
//
//        CSVUtils.createCSVFile(list, head, "D:/temp/", "56分局");
////        com.tydic.common.word.ExcelUtil.export2Excel();

    }

    public void select(String _index, String _type, String query) {


    }

    public void pageSelect(String _index, String _type, String query){

    }

    /**
     * 新增和更新数据
     * @param _index
     * @param _type
     * @param _id
     * @param jsonString
     * @return
     */
    public boolean insert(String _index, String _type, String _id, String jsonString){
        IndexResponse response = client.prepareIndex(_index, _type, _id).setSource(jsonString).get();
        return true;
    }

    public boolean insert(String _index, String _type, String _id, Map map){
        IndexResponse response = client.prepareIndex(_index, _type, _id).setSource(map).get();
        return true;
    }

    /**
     * 根据 id 删除数据
     * @param _index
     * @param _type
     * @param _id
     * @return
     */
    public boolean deleteByID(String _index, String _type, String _id){
        DeleteResponse response = client.prepareDelete(_index, _type, _id).get();
        return true;
    }

    /**
     * 删除索引
     * @param index
     * @return
     */
    public boolean deleteIndex(String... index){
        DeleteIndexResponse response = adminClient.prepareDelete(index).get();
        return response.isAcknowledged();
    }

    /**
     * 判断索引是否存在
     * @param index 索引
     * @return 存在 true, 不存在 false
     */
    public boolean indexExists(String index){
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        if(response.isExists()){
            return true;
        }
        return false;
    }

    @Test
    public void importUpdate() throws Exception {

//        String str = ExcelUtil.getExcelString(new FileInputStream(new File("D:\\work\\data\\56分局2.xlsx")));
//        System.out.println(str);
//        String[] arr = str.split("\n");
//        System.out.println(arr);
//        String[] head = arr[0].split("\t");
//
//        List<Map<String, Object>> list = new ArrayList<>();
//        for (int i = 0; i < arr.length; i++) {
//            if(i == 0){
//                continue;
//            }
//            String[] row = arr[i].split("\t");
//            Map<String, Object> map = new LinkedHashMap<>();
//            for(int j = 0; j < row.length; j++){
//                map.put(head[j], row[j]);
//            }
//            list.add(map);
//        }
//        System.out.println(list);
//
//        EsTest es = new EsTest();
//        for (Map<String, Object> map: list) {
//            // 更新数据
//            String id = map.get("ID").toString();
//            es.client.prepareIndex("case_jgh_info", "case_jgh_info", id).setSource(map).get();
//        }

    }

    @Test
    public void delete(){
        EsTest es = new EsTest();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        query.must(QueryBuilders.matchQuery("ZTKID", "1399").operator(MatchQueryBuilder.Operator.AND));
        SearchResponse response = es.client.prepareSearch("case_jgh_info")
                .setTypes("case_jgh_info")
                .setQuery(query)
                .setFrom(0)
                .setSize(1000)
                .execute().actionGet();

        SearchHits hits = response.getHits();
        System.out.println("查询总数: " + hits.getTotalHits());

//        for (SearchHit hit : hits) {
//            String id = hit.getId();
//            client.prepareDelete("case_jgh_info", "case_jgh_info", id).get();
//        }
    }


}
