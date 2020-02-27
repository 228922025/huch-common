package com.huch.common.test.apache;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

/**
 * @author huchanghua
 * @create 2020-02-09-18:26
 */
public class ArraysUtilsTest {

    @Test
    public void test(){
        int[] array = new int[]{1,2};
        //给数组添加一个新元素，返回新集合
        int[] array2 = ArrayUtils.add(array,3);
        //合并数组，其中可以是null,如果只有一个数组相当于copy
        int[] array3 = ArrayUtils.addAll(array,array2);
        int[] array4 = ArrayUtils.clone(array);
        boolean isHave = ArrayUtils.contains(array,1);
        int code = ArrayUtils.hashCode(array);
        //从array的第二个位置开始插入array4的值
        int[] array5 = ArrayUtils.insert(2,array,array4);
        //isEmpty判断是否是null或者长度0，isNotEmpty只是判断长度是不是0
        ArrayUtils.isEmpty(array);
        ArrayUtils.isNotEmpty(array);
        //判断长度是否一样，null的长度也是0
        ArrayUtils.isSameLength(array,array5);
        //设置数组中null元素为[]
        ArrayUtils.nullToEmpty(array5);

        //同上add
//        ArrayUtils.remove();
//        ArrayUtils.removeAll();
        //反转数组，反转数组指定区间值
        ArrayUtils.reverse(array5);
        ArrayUtils.reverse(array5,2,3);
        //打乱顺序
        ArrayUtils.shuffle(array5);
        //截取数组
        ArrayUtils.subarray(array5,2,3);
        //指定位置交换数据
        ArrayUtils.swap(array5,2,3);
        //创建新数组
        String[] strArray = ArrayUtils.toArray("大","小");
        //对象数据类型转换为基本数据类型
        Integer[] arrat7 = new Integer[]{5,6,7,8};
        int[] array6 = ArrayUtils.toPrimitive(arrat7);
        //转换成字符串
        String str = ArrayUtils.toString(arrat7);
    }
}
