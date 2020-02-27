package com.huch.common.test.classload;

/**
 * 父类
 *
 * @author huchanghua
 * @create 2019-03-10-11:16
 */
public class Parent {
    private static int ps;
    private int pv;

    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类代码块");
    }

    Parent() {
        System.out.println("父类构造函数");
    }
}
