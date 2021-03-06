package com.huch.common.io;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * 使用nio以提高性能
 */
public class NIOFileUtil {

    /**
     * 写数据到文件
     * @param in
     * @param target
     * @throws IOException
     */
    public static void writeToFile(InputStream in, File target) throws IOException {
        final int BUFFER = 1024;
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = in.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    /**
     * 写数据到文件
     * @param in
     * @param target
     * @throws IOException
     */
    public static void writeToFile1(InputStream in, File target) throws IOException {
        FileOutputStream fo = null;
        ReadableByteChannel src = null;
        FileChannel out = null;
        try {
            int len = in.available();
            src = Channels.newChannel(in);
            fo = new FileOutputStream(target);
            out = fo.getChannel();
            out.transferFrom(src, 0, len);
        } finally {
            if (fo != null) {
                fo.close();
            }
            if (src != null) {
                src.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 写byte数据到文件
     * @param data
     * @param target
     * @throws IOException
     */
    public static void writeToFile(byte[] data, File target) throws IOException {
        FileOutputStream fo = null;
        ReadableByteChannel src = null;
        FileChannel out = null;
        try {
            src = Channels.newChannel(new ByteArrayInputStream(data));
            fo = new FileOutputStream(target);
            out = fo.getChannel();
            out.transferFrom(src, 0, data.length);
        } finally {
            if (fo != null) {
                fo.close();
            }
            if (src != null) {
                src.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 复制文件
     *
     * @param source - 源文件
     * @param target - 目标文件
     */
    public static void copyFile(File source, File target) {
        FileInputStream fi = null;
        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {
            fi = new FileInputStream(source);

            fo = new FileOutputStream(target);

            in = fi.getChannel();// 得到对应的文件通道

            out = fo.getChannel();// 得到对应的文件通道

            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
