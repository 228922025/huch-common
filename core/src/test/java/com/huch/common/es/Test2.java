package com.huch.common.es;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author huchanghua
 * @create 2019-12-10-22:28
 */
public class Test2 {
    private static ESClient client = new ESClient("192.1.1.41", 9300, "huch-es");


    @Test
    public void searchAll(){
        List<Map<String, Object>> list = client.searchAll();
        System.out.println(list.size());
        System.out.println(client.searchAll());
    }


}
