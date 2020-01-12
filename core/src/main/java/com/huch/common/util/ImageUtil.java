package com.huch.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具类
 *
 * @author huch
 * @create 2019-03-19-22:23
 */
public class ImageUtil {


    /**
     * 写图片到指定路径下
     * @param path 指定目录
     * @param image 图片
     */
    public static void writeImage(String path, BufferedImage image) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(path + System.currentTimeMillis() + ".png");
            ImageIO.write(image, "png", out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换成矩形数组，获取RGB色
     * @param image
     * @return
     */
    public static int[][] getRGB(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] arr = new int[width][height];

        for (int h = 0; h < height; h++) {
            // 行扫描
            for (int w = 0; w < width; w++) {
                int dip = image.getRGB(w, h);
                arr[w][h] = dip;
            }
        }
        return arr;
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中的矩形位置
     * @param image
     * @param target
     * @return
     */
    public static Rectangle findImage4full(BufferedImage image, BufferedImage target) {
        int sw = image.getWidth();
        int sh = image.getHeight();

        int tw = target.getWidth();
        int th = target.getHeight();

        // 目标图片4脚色值
        int[] tDips = new int[4];
        tDips[0] = target.getRGB(0, 0);
        tDips[1] = target.getRGB(tw - 1, 0);
        tDips[2] = target.getRGB(0, th - 1);
        tDips[3] = target.getRGB(tw-1, th-1);

        // 扫描高
        for(int y = 0; y < sh-th; y++){
            // 扫描行
            for (int x = 0; x < sw-tw; x++) {
                // 左上角
                int dip = image.getRGB(x, y);
                if(dip != tDips[0]){
                    continue;
                }
                // 右上角
                dip = image.getRGB(x + tw - 1, y);
                if(dip != tDips[1]){
                    continue;
                }
                // 左下角
                dip = image.getRGB(x, y + th - 1);
                if (dip != tDips[2]) {
                    continue;
                }
                // 右下角
                dip = image.getRGB(x + tw - 1, y + th - 1);
                if (dip != tDips[3]) {
                    continue;
                }

                return new Rectangle(x, y, tw, tw);
            }
        }
        return null;
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中的矩形位置
     * @param src
     * @param target
     * @return
     */
    public static Rectangle findImage4full(int[][] src, int[][] target) {
        int sw = src.length;
        int sh = src[0].length;

        int tw = target.length;
        int th = target[0].length;

        // 目标图片4脚色值
        int[] tDips = new int[4];
        tDips[0] = target[0][0];
        tDips[1] = target[tw-1][0];
        tDips[2] = target[0][th-1];
        tDips[3] = target[tw-1][th-1];

        // 扫描高
        for(int y = 0; y < sh-th; y++){
            // 扫描行
            for (int x = 0; x < sw-tw; x++) {
                // 左上角
                int dip = src[x][y];
                if(dip != tDips[0]){
                    continue;
                }
                // 右上角
                dip = src[x+tw-1][y];
                if(dip != tDips[1]){
                    continue;
                }
                // 左下角
                dip = src[x][y+th-1];
                if (dip != tDips[2]) {
                    continue;
                }
                // 右下角
                dip = src[x+tw-1][y+th-1];
                if (dip != tDips[3]) {
                    continue;
                }

                return new Rectangle(x, y, tw, tw);
            }
        }
        return null;
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中心点坐标
     * @param src
     * @param target
     * @return
     */
    public static Point findImage4FullPoint(int[][] src, int[][] target) {
        Rectangle rect = findImage4full(src, target);
        return rectangleCenterPonit(rect);
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中的矩形位置
     * @param src
     * @param target
     * @return
     */
    public static List<Rectangle> findAllImage4full(int[][] src, int[][] target) {
        List<Rectangle> list = new ArrayList<>();
        int sw = src.length;
        int sh = src[0].length;

        int tw = target.length;
        int th = target[0].length;

        // 目标图片4脚色值
        int[] tDips = new int[4];
        tDips[0] = target[0][0];
        tDips[1] = target[tw-1][0];
        tDips[2] = target[0][th-1];
        tDips[3] = target[tw-1][th-1];

        // 扫描高
        for(int y = 0; y < sh-th; y++){
            // 扫描行
            for (int x = 0; x < sw-tw; x++) {
                // 左上角
                int dip = src[x][y];
                if(dip != tDips[0]){
                    continue;
                }
                // 右上角
                dip = src[x+tw-1][y];
                if(dip != tDips[1]){
                    continue;
                }
                // 左下角
                dip = src[x][y+th-1];
                if (dip != tDips[2]) {
                    continue;
                }
                // 右下角
                dip = src[x+tw-1][y+th-1];
                if (dip != tDips[3]) {
                    continue;
                }
                list.add(new Rectangle(x, y, tw, tw));

                // 将找到的图片区域改变颜色
                for (int ty = 0; ty < th; ty++) {
                    for (int tx = 0; tx < tw; tx++) {
                        src[x+tx][y+ty] = 0;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中的中心点坐标
     * @param src
     * @param target
     * @return
     */
    public static List<Point> findAllImage4fullPoint(int[][] src, int[][] target) {
        List<Point> list = new ArrayList<>();
        List<Rectangle> data = findAllImage4full(src, target);

        for (Rectangle rect : data) {
            list.add(rectangleCenterPonit(rect));
        }
        return list;
    }

    /**
     * 查找图片，以目标图片的8个角为点识别
     * @param image
     * @param target
     * @return
     */
    public static Rectangle findImage8full(BufferedImage image, BufferedImage target) {
        int sw = image.getWidth();
        int sh = image.getHeight();
        // 目标图片宽高
        int tw = target.getWidth();
        int th = target.getHeight();
        // 目标图片4脚色值
        int[] tDips = new int[8];
        tDips[0] = target.getRGB(0, 0);
        tDips[1] = target.getRGB(tw - 1, 0);
        tDips[2] = target.getRGB(0, th - 1);
        tDips[3] = target.getRGB(tw-1, th-1);
        // 内4角
        tDips[4] = target.getRGB(1, 1);
        tDips[5] = target.getRGB(tw - 2, 1);
        tDips[6] = target.getRGB(1, th - 2);
        tDips[7] = target.getRGB(tw-2, th-2);

        for(int y = 0; y < sh-th; y++){
            // 扫描行
            for (int x = 0; x < sw-tw; x++) {
                // 左上角
                int dip = image.getRGB(x, y);
                if (dip != tDips[0]) {
                    continue;
                }
                // 右上角
                dip = image.getRGB(x+tw-1, y);
                if (dip != tDips[1]) {
                    continue;
                }
                // 左下角
                dip = image.getRGB(x, y+th-1);
                if (dip != tDips[2]) {
                    continue;
                }
                // 右下角
                dip = image.getRGB(x+tw-1, y+th-1);
                if (dip != tDips[3]) {
                    continue;
                }

                // 内4角
                dip = image.getRGB(x+1, y+1);
                if (dip != tDips[4]) {
                    continue;
                }
                // 右上角
                dip = image.getRGB(x+tw-2, y+1);
                if (dip != tDips[5]) {
                    continue;
                }
                // 左下角
                dip = image.getRGB(x+1, y+th-2);
                if (dip != tDips[6]) {
                    continue;
                }
                // 右下角
                dip = image.getRGB(x+tw-2, y+th-2);
                if (dip != tDips[7]) {
                    continue;
                }
                return new Rectangle(x, y, tw, th);
            }
        }
        return null;
    }

    /**
     * 查找图片，以目标图片的8个角为点识别
     * @param src
     * @param target
     * @return
     */
    public static Rectangle findImage8full(int[][] src, int[][] target) {
        int sw = src.length;
        int sh = src[0].length;

        int tw = target.length;
        int th = target[0].length;

        // 目标图片4脚色值
        int[] tDips = new int[8];
        tDips[0] = target[0][0];
        tDips[1] = target[tw-1][0];
        tDips[2] = target[0][th-1];
        tDips[3] = target[tw-1][th-1];

        tDips[4] = target[1][1];
        tDips[5] = target[tw-2][1];
        tDips[6] = target[1][th-2];
        tDips[7] = target[tw-2][th-2];

        for(int y = 0; y < sh-th; y++){
            // 扫描行
            for (int x = 0; x < sw-tw; x++) {
                // 左上角
                int dip = src[x][y];
                if(dip != tDips[0]){
                    continue;
                }
                // 右上角
                dip = src[x + tw - 1][y];
                if(dip != tDips[1]){
                    continue;
                }
                // 左下角
                dip = src[x][y + th - 1];
                if (dip != tDips[2]) {
                    continue;
                }
                // 右下角
                dip = src[x + tw - 1][y + th - 1];
                if (dip != tDips[3]) {
                    continue;
                }

                // 内4角
                dip = src[x+1][y+1];
                if(dip != tDips[4]){
                    continue;
                }
                // 右上角
                dip = src[x + tw - 2][y+1];
                if(dip != tDips[5]){
                    continue;
                }
                // 左下角
                dip = src[x+1][y + th - 2];
                if (dip != tDips[6]) {
                    continue;
                }
                // 右下角
                dip = src[x + tw - 2][y + th - 2];
                if (dip != tDips[7]) {
                    continue;
                }
                return new Rectangle(x, y, tw, th);
            }
        }

        return null;
    }

    /**
     * 在图片中查找目标图片，返回目标图片在原图中心点坐标
     * @param src
     * @param target
     * @return
     */
    public static Point findImage8FullPoint(int[][] src, int[][] target) {
        Rectangle rect = findImage8full(src, target);
        return rectangleCenterPonit(rect);
    }

    /**
     * 获取矩形中心点坐标
     * @param rectangle
     * @return
     */
    public static Point rectangleCenterPonit(Rectangle rectangle) {
        if (rectangle == null) {
            return null;
        }
        return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
    }








}
