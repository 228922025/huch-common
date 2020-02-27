package com.huch.common.test.apache;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2020-02-09-18:45
 */
public class RandomUtilsTest {
    @Test
    public void test() {
        //返回一个随机布尔值
        RandomUtils.nextBoolean();
        //随机生成10个长的的byte数组
        byte[] bytes = RandomUtils.nextBytes(10);
        //指定范围随机数1-5 不包括5
        RandomUtils.nextInt(1,5);
        //1-4.99999之间的随机数，nextLong，nextFloat
        RandomUtils.nextDouble(1,5);
    }
}
