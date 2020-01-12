package com.huch.common.util;


/**
 * ID生成器工具类，此工具类中主要封装：
 * 
 * <pre>
 * 1. 唯一性ID生成器：UUID、ObjectId（MongoDB）、Snowflake
 * </pre>
 * 
 * <p>
 * ID相关文章见：http://calvin1978.blogcn.com/articles/uuid.html
 * 
 * @author looly
 * @since 4.1.13
 */
public class IdUtil {

	// ------------------------------------------------------------------- UUID
	/**
	 * 获取随机UUID
	 * 
	 * @return 随机UUID
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 简化的UUID，去掉了横线
	 * 
	 * @return 简化的UUID，去掉了横线
	 */
	public static String simpleUUID() {
		return UUID.randomUUID().toString(true);
	}
	
	/**
	 * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
	 * 
	 * @return 随机UUID
	 * @since 4.1.19
	 */
	public static String fastUUID() {
		return UUID.fastUUID().toString();
	}
	
	/**
	 * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
	 * 
	 * @return 简化的UUID，去掉了横线
	 * @since 4.1.19
	 */
	public static String fastSimpleUUID() {
		return UUID.fastUUID().toString(true);
	}


}
