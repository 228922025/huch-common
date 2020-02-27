package com.huch.common.test.classload;

import com.huch.common.crypto.MD5;

/**
 * 1.在初次new一个Child类对象时，发现其有父类，则先加载Parent类，再加载Child类。
 * 2.加载Parent类：
 *   初始化Parent类的static属性，赋默认值；
 *   执行Parent类的static初始化块；
 * 3.加载Child类：
 *   初始化Child类的static属性，赋默认值；
 *   执行Child类的static初始化块；
 * 4.创建Parent类对象：
 *   初始化Parent类的非static属性，赋默认值；
 *   执行Parent类的instance初始化块；
 *   执行Parent类的构造方法；
 * 5.创建Child类对象：
 *   初始化Child类的非static属性，赋默认值；
 *   执行Child类的instance初始化块；
 *   执行Child类的构造方法；
 * 6.后面再创建Child类对象时，就按照顺序执行（4）（5）两步。
 * @author huchanghua
 * @create 2019-03-10-11:17
 */
public class Test {

    public static void main(String[] args) {
        String str = "123";

        System.out.println(MD5.md5(str));
    }
}
