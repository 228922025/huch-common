package com.huch.common.test.apache;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2020-02-09-18:44
 */
public class StringUtilsTest {
    @Test
    public void test(){
        //2只显示6个长的的字符串，多的用省略号
        String str1 = StringUtils.abbreviate("abcdefg",6);
        //显示5个长度的字符串，除了最后一个中间用设置的符号代替
        String str2 = StringUtils.abbreviateMiddle("abcdefgmmssmm","--",5);
        //拼接字符串，但是str的结尾不能和def重合
        StringUtils.appendIfMissing("abc","def");
        //设置字符串长的，不足在两边用指定字符填充
        StringUtils.center("hy",8,"*");
        //检测字符串abcde中是否含有abc
        StringUtils.contains("abcde","abc");
        //检测字符串abcdef中是否保护abm或者y
        StringUtils.containsAny("abcdef","abm","y");
        //为null或者空字符串返回空字符串,有其他字符，返回字符
        String str3 = StringUtils.defaultString(null);
        //检测字符以什么结尾
        StringUtils.endsWith("agffsww","sww");
        StringUtils.equals("aaa","bbb");
        //检测为空
        StringUtils.isEmpty("");
        //把数组以固定字符连接，组成字符串char、double、float、int、long、short、object、T同理
        String str5 = StringUtils.join(new String[]{"a","b","c","d"},":");
        //把字符串的重复此时控制在指定大小
        StringUtils.repeat("sss",2);
        //替换所有b为*
        StringUtils.replace("abcd","b","*");
        StringUtils.reverse("abcd");
    }
}
