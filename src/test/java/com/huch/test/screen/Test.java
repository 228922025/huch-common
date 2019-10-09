/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Test
 * Author:   huch
 * Date:     2019/3/13 22:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.huch.test.screen;

import com.huch.common.screen.ScreenUtil;

import java.io.File;
import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author huch
 * @create 2019/3/13
 * @since 1.0.0
 */
public class Test {

    public static void main(String[] args) throws IOException {
//        File file = new File("d:/image/a.png");
//        ScreenUtil.writeScreenImage(file);

        File file = new File("HelloWorld.java");
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.out.println(suffix);

    }




}
 
