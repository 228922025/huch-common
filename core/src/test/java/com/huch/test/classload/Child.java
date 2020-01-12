package com.huch.test.classload;

/**
 * 子类
 *
 * @author huchanghua
 * @create 2019-03-10-11:17
 */
public class Child extends Parent{
    private static int cs;
    private int cv;

    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类代码块");
    }

    Child() {
        System.out.println("子类构造函数");
    }
}
