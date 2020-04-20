package com.huch.common.io;

import java.io.*;

/**
 * @author huchanghua
 * @create 2019-06-12-15:29
 */
public class StreamUtil {

    private StreamUtil() {
    }

    /**
     * 读取文件内容到byte数组
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] readContentFromFile(String filePath) throws IOException {
        FileInputStream fin = new FileInputStream(filePath);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        int n = -1;
        byte[] buf = new byte[1024];
        while ((n = fin.read(buf)) != -1) {
            os.write(buf, 0, n);
        }
        fin.close();
        return os.toByteArray();
    }

    public static byte[] readContentFromInputStream(InputStream in) throws IOException {
        return readContentFromInputStream(in, Integer.MAX_VALUE);
    }

    /**
     * 从输入流读取数据
     * @param in
     * @param length
     * @return
     * @throws IOException
     */
    public static byte[] readContentFromInputStream(InputStream in, int length) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (os.size() < length) {
            int n = in.read(buf);
            if (n > 0) {
                os.write(buf, 0, n);

            } else if (n < 0) {
                break;
            }
        }
        in.close();
        return os.toByteArray();
    }

    /**
     * 将byte数组内容写入到文件中
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeContentToFile(String filePath, byte[] content) throws IOException {
        OutputStream out = new FileOutputStream(filePath);
        out.write(content);
        out.close();
    }

    /**
     * 将输入流的内容全部输出的到输出流
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if(bytesRead == -1){
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }

    }



}
