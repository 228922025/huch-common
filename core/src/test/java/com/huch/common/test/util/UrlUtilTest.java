package com.huch.common.test.util;

import com.huch.common.util.URLUtil;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2019-09-28-20:19
 */
public class UrlUtilTest {


    @Test
    public void test(){
        String url = "http://www.pokemon.name/wiki/白雾（招式）?name=name";
        String url2 = "http://www.pokemon.name/wiki/白雾（招式）#";

        System.out.println(URLUtil.encode(url));
        System.out.println(URLUtil.decode(URLUtil.encode(url)));
    }
}
