package com.huch.test.io;

import com.huch.common.io.FileUtil;
import com.huch.common.io.NIOFileUtil;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huchanghua
 * @create 2019-04-22-09:08
 */
public class FileTest {

    @Test
    public void lsTest(){
        File[] files = FileUtil.ls("/");
        System.out.println(files);
        for(File f: files){
            System.out.println(f.getAbsolutePath());
        }
    }

    @Test
    public void isEmpty(){
        File file = new File("/Users/huchanghua/Documents/worker/a");
        System.out.println(FileUtil.isEmpty(file));
    }

    @Test
    public void loopFiles(){
        String path = "/Users/huchanghua/Documents/";
        List<File> list = FileUtil.loopFiles(path);
        System.out.println(list);
    }

    @Test
    public void listFile(){
        File file = new File("/Users/huchanghua/Documents/develop");
        File[] fileArr = file.listFiles();
        for (File item : fileArr) {
            System.out.println(item);
        }
    }

    @Test
    public void writeFileTest(){
        File file = new File("/Users/huchanghua/Documents/test/jdk-8u191-linux-x64.tar.gz");
        long len = file.length();
        byte[] data = new byte[(int) len];
        InputStream in;
        try {
            in = new FileInputStream(file);
            in.read(data);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "/Users/huchanghua/Documents/test/01";
        String str = "测试文本 测试文本\n";
        List<String> list = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
            list.add(str);
        }
        long startTime = System.currentTimeMillis();
        FileUtil.writeFile(path, new String(data));

        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "毫秒");

    }

    @Test
    public void nioWTest() throws IOException {
        File file = new File("/Users/huchanghua/Documents/test/jdk-8u191-linux-x64.tar.gz");
        File target = new File("/Users/huchanghua/Documents/test/02");

        long startTime = System.currentTimeMillis();
        NIOFileUtil.writeToFile(new FileInputStream(file), target);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "毫秒");


    }

    @Test
    public void nioCopy(){
        File file = new File("/Users/huchanghua/Documents/test/jdk-8u191-linux-x64.tar.gz");
        File file2 = new File("/Users/huchanghua/Documents/test/01.gz");

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 10; i++){
            NIOFileUtil.copyFile(file, new File("/Users/huchanghua/Documents/test/" + i + ".gz"));
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "毫秒");
    }
}
