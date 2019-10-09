package com.huch.common.util;

import com.huch.common.convert.BasicType;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.*;

/**
 * 类工具类 <br>
 * 
 * @author xiaoleilu
 *
 */
public class ClassUtil {

	/**
	 * {@code null}安全的获取对象类型
	 * 
	 * @param <T> 对象类型
	 * @param obj 对象，如果为{@code null} 返回{@code null}
	 * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClass(T obj) {
		return ((null == obj) ? null : (Class<T>) obj.getClass());
	}
	
	/**
	 * 获得外围类<br>
	 * 返回定义此类或匿名类所在的类，如果类本身是在包中定义的，返回{@code null}
	 * 
	 * @param clazz 类
	 * @return 外围类
	 * @since 4.5.7
	 */
	public static Class<?> getEnclosingClass(Class<?> clazz) {
		return null == clazz ? null : clazz.getEnclosingClass();
	}
	
	/**
	 * 是否为顶层类，既定义在包中的类，而非定义在类中的内部类
	 * @param clazz 类
	 * @return 是否为顶层类
	 * @since 4.5.7
	 */
	public static boolean isTopLevelClass(Class<?> clazz) {
		if(null == clazz) {
			return false;
		}
		return null == getEnclosingClass(clazz);
	}

	/**
	 * 获取类名
	 * 
	 * @param obj 获取类名对象
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 * @since 3.0.7
	 */
	public static String getClassName(Object obj, boolean isSimple) {
		if (null == obj) {
			return null;
		}
		final Class<?> clazz = obj.getClass();
		return getClassName(clazz, isSimple);
	}

	/**
	 * 获取类名<br>
	 * 类名并不包含“.class”这个扩展名<br>
	 * 例如：ClassUtil这个类<br>
	 * 
	 * <pre>
	 * isSimple为false: "com.xiaoleilu.hutool.util.ClassUtil"
	 * isSimple为true: "ClassUtil"
	 * </pre>
	 * 
	 * @param clazz 类
	 * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
	 * @return 类名
	 * @since 3.0.7
	 */
	public static String getClassName(Class<?> clazz, boolean isSimple) {
		if (null == clazz) {
			return null;
		}
		return isSimple ? clazz.getSimpleName() : clazz.getName();
	}

	/**
	 * 获取完整类名的短格式如：<br>
	 * cn.hutool.core.util.StrUtil -》c.h.c.u.StrUtil
	 * 
	 * @param className 类名
	 * @return 短格式类名
	 * @since 4.1.9
	 */
	public static String getShortClassName(String className) {
		final List<String> packages = StrUtil.splitToList(className, CharUtil.DOT);
		if (null == packages || packages.size() < 2) {
			return className;
		}

		final int size = packages.size();
		final StringBuilder result = new StringBuilder();
		result.append(packages.get(0).charAt(0));
		for (int i = 1; i < size - 1; i++) {
			result.append(CharUtil.DOT).append(packages.get(i).charAt(0));
		}
		result.append(CharUtil.DOT).append(packages.get(size - 1));
		return result.toString();
	}

	/**
	 * 获得对象数组的类数组
	 * 
	 * @param objects 对象数组，如果数组中存在{@code null}元素，则此元素被认为是Object类型
	 * @return 类数组
	 */
	public static Class<?>[] getClasses(Object... objects) {
		Class<?>[] classes = new Class<?>[objects.length];
		Object obj;
		for (int i = 0; i < objects.length; i++) {
			obj = objects[i];
			classes[i] = (null == obj) ? Object.class : obj.getClass();
		}
		return classes;
	}

	/**
	 * 指定类是否与给定的类名相同
	 * 
	 * @param clazz 类
	 * @param className 类名，可以是全类名（包含包名），也可以是简单类名（不包含包名）
	 * @param ignoreCase 是否忽略大小写
	 * @return 指定类是否与给定的类名相同
	 * @since 3.0.7
	 */
	public static boolean equals(Class<?> clazz, String className, boolean ignoreCase) {
		if (null == clazz || StrUtil.isBlank(className)) {
			return false;
		}
		if (ignoreCase) {
			return className.equalsIgnoreCase(clazz.getName()) || className.equalsIgnoreCase(clazz.getSimpleName());
		} else {
			return className.equals(clazz.getName()) || className.equals(clazz.getSimpleName());
		}
	}

	// ---------------------------------------------------------------------------------------------------- Invoke end

	/**
	 * 是否为包装类型
	 *
	 * @param clazz 类
	 * @return 是否为包装类型
	 */
	public static boolean isPrimitiveWrapper(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return BasicType.wrapperPrimitiveMap.containsKey(clazz);
	}

	/**
	 * 是否为基本类型（包括包装类和原始类）
	 *
	 * @param clazz 类
	 * @return 是否为基本类型
	 */
	public static boolean isBasicType(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
	}

	/**
	 * 是否简单值类型或简单值类型的数组<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class及其数组
	 *
	 * @param clazz 属性类
	 * @return 是否简单值类型或简单值类型的数组
	 */
	public static boolean isSimpleTypeOrArray(Class<?> clazz) {
		if (null == clazz) {
			return false;
		}
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * 是否为简单值类型<br>
	 * 包括：原始类型,、String、other CharSequence, a Number, a Date, a URI, a URL, a Locale or a Class.
	 *
	 * @param clazz 类
	 * @return 是否为简单值类型
	 */
	public static boolean isSimpleValueType(Class<?> clazz) {
		return isBasicType(clazz) //
				|| clazz.isEnum() //
				|| CharSequence.class.isAssignableFrom(clazz) //
				|| Number.class.isAssignableFrom(clazz) //
				|| Date.class.isAssignableFrom(clazz) //
				|| clazz.equals(URI.class) //
				|| clazz.equals(URL.class) //
				|| clazz.equals(Locale.class) //
				|| clazz.equals(Class.class);//
	}

	/**
	 * 检查目标类是否可以从原类转化<br>
	 * 转化包括：<br>
	 * 1、原类是对象，目标类型是原类型实现的接口<br>
	 * 2、目标类型是原类型的父类<br>
	 * 3、两者是原始类型或者包装类型（相互转换）
	 *
	 * @param targetType 目标类型
	 * @param sourceType 原类型
	 * @return 是否可转化
	 */
	public static boolean isAssignable(Class<?> targetType, Class<?> sourceType) {
		if (null == targetType || null == sourceType) {
			return false;
		}

		// 对象类型
		if (targetType.isAssignableFrom(sourceType)) {
			return true;
		}

		// 基本类型
		if (targetType.isPrimitive()) {
			// 原始类型
			Class<?> resolvedPrimitive = BasicType.wrapperPrimitiveMap.get(sourceType);
			if (resolvedPrimitive != null && targetType.equals(resolvedPrimitive)) {
				return true;
			}
		} else {
			// 包装类型
			Class<?> resolvedWrapper = BasicType.primitiveWrapperMap.get(sourceType);
			if (resolvedWrapper != null && targetType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定类是否为Public
	 *
	 * @param clazz 类
	 * @return 是否为public
	 */
	public static boolean isPublic(Class<?> clazz) {
		if (null == clazz) {
			throw new NullPointerException("Class to provided is null.");
		}
		return Modifier.isPublic(clazz.getModifiers());
	}

	/**
	 * 指定方法是否为Public
	 *
	 * @param method 方法
	 * @return 是否为public
	 */
	public static boolean isPublic(Method method) {
		Assert.notNull(method, "Method to provided is null.");
		return Modifier.isPublic(method.getModifiers());
	}

	/**
	 * 指定类是否为非public
	 *
	 * @param clazz 类
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Class<?> clazz) {
		return false == isPublic(clazz);
	}

	/**
	 * 指定方法是否为非public
	 *
	 * @param method 方法
	 * @return 是否为非public
	 */
	public static boolean isNotPublic(Method method) {
		return false == isPublic(method);
	}

	/**
	 * 是否为静态方法
	 *
	 * @param method 方法
	 * @return 是否为静态方法
	 */
	public static boolean isStatic(Method method) {
		Assert.notNull(method, "Method to provided is null.");
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * 设置方法为可访问
	 *
	 * @param method 方法
	 * @return 方法
	 */
	public static Method setAccessible(Method method) {
		if (null != method && false == method.isAccessible()) {
			method.setAccessible(true);
		}
		return method;
	}

	/**
	 * 是否为抽象类
	 *
	 * @param clazz 类
	 * @return 是否为抽象类
	 */
	public static boolean isAbstract(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}

	/**
	 * 是否为标准的类<br>
	 * 这个类必须：
	 *
	 * <pre>
	 * 1、非接口
	 * 2、非抽象类
	 * 3、非Enum枚举
	 * 4、非数组
	 * 5、非注解
	 * 6、非原始类型（int, long等）
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 是否为标准类
	 */
	public static boolean isNormalClass(Class<?> clazz) {
		return null != clazz //
				&& false == clazz.isInterface() //
				&& false == isAbstract(clazz) //
				&& false == clazz.isEnum() //
				&& false == clazz.isArray() //
				&& false == clazz.isAnnotation() //
				&& false == clazz.isSynthetic() //
				&& false == clazz.isPrimitive();//
	}

	/**
	 * 判断类是否为枚举类型
	 *
	 * @param clazz 类
	 * @return 是否为枚举类型
	 * @since 3.2.0
	 */
	public static boolean isEnum(Class<?> clazz) {
		return null == clazz ? false : clazz.isEnum();
	}

	/**
	 * 获得给定类所在包的名称<br>
	 * 例如：<br>
	 * com.xiaoleilu.hutool.util.ClassUtil =》 com.xiaoleilu.hutool.util
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackage(Class<?> clazz) {
		if (clazz == null) {
			return StrUtil.EMPTY;
		}
		final String className = clazz.getName();
		int packageEndIndex = className.lastIndexOf(StrUtil.DOT);
		if (packageEndIndex == -1) {
			return StrUtil.EMPTY;
		}
		return className.substring(0, packageEndIndex);
	}

	/**
	 * 获得给定类所在包的路径<br>
	 * 例如：<br>
	 * com.xiaoleilu.hutool.util.ClassUtil =》 com/xiaoleilu/hutool/util
	 *
	 * @param clazz 类
	 * @return 包名
	 */
	public static String getPackagePath(Class<?> clazz) {
		return getPackage(clazz).replace(StrUtil.C_DOT, StrUtil.C_SLASH);
	}

	/**
	 * 获取指定类型分的默认值<br>
	 * 默认值规则为：
	 *
	 * <pre>
	 * 1、如果为原始类型，返回0
	 * 2、非原始类型返回{@code null}
	 * </pre>
	 *
	 * @param clazz 类
	 * @return 默认值
	 * @since 3.0.8
	 */
	public static Object getDefaultValue(Class<?> clazz) {
		if (clazz.isPrimitive()) {
			if (long.class == clazz) {
				return 0L;
			} else if (int.class == clazz) {
				return 0;
			} else if (short.class == clazz) {
				return (short) 0;
			} else if (char.class == clazz) {
				return (char) 0;
			} else if (byte.class == clazz) {
				return (byte) 0;
			} else if (double.class == clazz) {
				return 0D;
			} else if (float.class == clazz) {
				return 0f;
			} else if (boolean.class == clazz) {
				return false;
			}
		}

		return null;
	}

	/**
	 * 获得默认值列表
	 *
	 * @param classes 值类型
	 * @return 默认值列表
	 * @since 3.0.9
	 */
	public static Object[] getDefaultValues(Class<?>... classes) {
		final Object[] values = new Object[classes.length];
		for (int i = 0; i < classes.length; i++) {
			values[i] = getDefaultValue(classes[i]);
		}
		return values;
	}
}