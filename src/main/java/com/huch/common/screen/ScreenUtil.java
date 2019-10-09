/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: ScreenUtil
 * Author:   huch
 * Date:     2019/3/13 21:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.huch.common.screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 屏幕工具类 用于截取图片
 * 〈〉
 *
 * @author huch
 * @create 2019/3/13
 * @since 1.0.0
 */
public class ScreenUtil {
    public static Dimension dimension;
    public static final Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        dimension = Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getWidth() {
        return dimension.width;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getHeight() {
        return dimension.height;
    }

    /**
     * 获取屏幕的矩形
     * @return 屏幕的矩形
     */
    public static Rectangle getRectangle() {
        return new Rectangle(getWidth(), getHeight());
    }

    /**
     * 获取屏幕图片,截取屏幕
     * @return
     */
    public static BufferedImage getScreenImage(){
        int width = dimension.width;
        int height = dimension.height;
        BufferedImage image = robot.createScreenCapture(new Rectangle(0,0, width, height));
        return image;
    }

    /**
     * 获取一个矩形区域的图片
     * @param screenRect
     * @return
     */
    public static BufferedImage getScreenImage(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }

    /**
     * 获取一个矩形区域的图片 x, y 从0开始
     * @param x x坐标
     * @param y y坐标
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage getScreenImage(int x, int y, int width, int height) {
        return robot.createScreenCapture(new Rectangle(x, y, width, height));
    }

    /**
     * 截取屏幕图片并保存到文件
     * @param outFile
     * @return
     * @throws IOException
     */
    public static File writeScreenImage(File outFile) throws IOException {
        BufferedImage image = getScreenImage();
        OutputStream out = null;
        try {
            out = new FileOutputStream(outFile);
            ImageIO.write(image, "png", outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            out.close();
        }
        return outFile;
    }
}
 
