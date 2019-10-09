package com.huch.test.util;

import com.alibaba.fastjson.JSONObject;
import com.huch.common.crypto.Base64;
import com.huch.common.io.FileUtil;
import com.huch.common.util.CharsetUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author huchanghua
 * @create 2019-06-11-21:21
 */
public class Base64Test {

    @Test
    public void test(){
        String str = "hUxdITUSRlSXlEbJSdggvg==";
        String enstr = "";
        enstr = Base64.encode(str);
        System.out.println(enstr);
        System.out.println(new String(Base64.decode(enstr)));

    }

    @Test
    public void test1(){
        String str = FileUtil.readString("/Users/huchanghua/Desktop/keepyou.txt");
        JSONObject jsonObject = JSONObject.parseObject(str);
        System.out.println(jsonObject);
        JSONObject json2 = jsonObject.getJSONObject("results");
        System.out.println(json2);

        // String enstr = json2.getString("data");
        // System.out.println(json2.getString("data"));
        //
        // System.out.println(Base64.decodeStr(enstr));

        Set<String> set = json2.keySet();
        System.out.println(set);
        for (Iterator<String> iter = set.iterator(); iter.hasNext(); ) {
            String enstr = json2.getJSONObject(iter.next()).getString("data");
            System.out.println(enstr);
            System.out.println(Base64.decodeStr(enstr));
            System.out.println(new String(java.util.Base64.getDecoder().decode(enstr)));
        }
    }

    @Test
    public void test2(){
        String str = "我们是一只小小鸟九十";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Base64.encode(str);
        }
        long end = System.currentTimeMillis();
        System.out.println("用时: " + (end - start));
    }

    @Test
    public void test3(){
        String str = "我们是一只小小鸟九十";
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            java.util.Base64.getEncoder().encode(str.getBytes());
        }
        long end = System.currentTimeMillis();
        System.out.println("用时: " + (end - start));
    }

    @Test
    public void decode() throws UnsupportedEncodingException {
        String str = "hUxdITUSRlSXlEbJSdggvg==";
        System.out.println(new String(java.util.Base64.getDecoder().decode(str), CharsetUtil.ISO_8859_1));
    }
}
