package com.huch.common.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-12-10-21:46
 */
public class ESUtil {

    /**
     * 解析查询数据到List
     * @param response
     * @return
     */
    public static List<Map<String, Object>> parseToMap(SearchResponse response){
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


}
