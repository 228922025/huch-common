package com.huch.common.test.apache;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2020-02-09-18:42
 */
public class ObjectUtilsTest {
    @Test
    public void test(){
        //判断一个或多个元素不为空
        boolean no1 = ObjectUtils.allNotNull(null,new Object());
        //至少有一个元素不为空
        boolean no2 = ObjectUtils.anyNotNull(null,new Object(),null);
        //调用的是类的clone方法，如果是集合，会把集合的下一层拷贝一份（一层深层拷贝）
        Object object1 = ObjectUtils.clone(new Object());
        //返回一个不为Null的对象
        ObjectUtils.firstNonNull(null,null,new Object());
    }
}
