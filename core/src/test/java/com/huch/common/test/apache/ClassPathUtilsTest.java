package com.huch.common.test.apache;

import org.apache.commons.lang3.ClassPathUtils;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2020-02-09-18:38
 */
public class ClassPathUtilsTest {
    @Test
    public void test(){
        //获取指定类路径,com.stude.arithmetic.long3.ArrayUtilsTest
        String path = ClassPathUtils.toFullyQualifiedName(ArraysUtilsTest.class,"ArrayUtilsTest");
        //com/stude/arithmetic/long3/ArrayUtilsTest
        String path2 = ClassPathUtils.toFullyQualifiedPath(ArraysUtilsTest.class,"ArrayUtilsTest");

        System.out.println(path);
        System.out.println(path2);
    }
}
