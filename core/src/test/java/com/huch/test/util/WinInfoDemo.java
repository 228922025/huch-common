/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: WinInfoDemo
 * Author:   huch
 * Date:     2019/3/13 20:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.huch.test.util;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author huch
 * @create 2019/3/13
 * @since 1.0.0
 */
public class WinInfoDemo {

    public static void main(String[] args) {
        String procCmd = System.getenv("windir")
                + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,"
                + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
        try {
            Process proc = Runtime.getRuntime().exec(procCmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
 
