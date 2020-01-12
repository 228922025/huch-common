package com.huch.test.util;

import com.huch.common.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author huchanghua
 * @create 2019-06-13-09:53
 */
public class FileUtilTest {

    @Test
    public void readTest(){
        String path = "/Users/huchanghua/Desktop";
        for (File file : FileUtil.ls(path)) {
            System.out.println(file.getAbsolutePath());
        }
    }

    @Test
    public void readStringTest(){
        String path = "/Users/huchanghua/Desktop/国外名人.txt";
        String content = FileUtil.readString(path);
        // System.out.println(content);
        String[] arr = content.split("\n");
        for (String tem : arr) {
            System.out.println(tem);
        }
        System.out.println(arr);
    }

    @Test
    public void readStringToListTest(){
        String path = "/Users/huchanghua/Desktop/国外名人.txt";
        List<String> list = FileUtil.readStringToList(path);
        for (String tem : list) {
            System.out.println(tem);
        }
    }

    @Test
    public void test(){
        String path = this.getClass().getResource("/").getPath();
        path += "国外名人.txt";
        System.out.println(path);
        List<String> list = FileUtil.readStringToList(path);
        for (String tem : list) {
            System.out.println(tem);
        }
    }
}
