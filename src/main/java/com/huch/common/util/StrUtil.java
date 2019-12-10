package com.huch.common.util;


import com.huch.common.text.StrFormatter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * @author xiaoleilu
 *
 */
public class StrUtil {

	public static final int INDEX_NOT_FOUND = -1;

	public static final char C_SPACE = CharUtil.SPACE;
	public static final char C_TAB = CharUtil.TAB;
	public static final char C_DOT = CharUtil.DOT;
	public static final char C_SLASH = CharUtil.SLASH;
	public static final char C_BACKSLASH = CharUtil.BACKSLASH;
	public static final char C_CR = CharUtil.CR;
	public static final char C_LF = CharUtil.LF;
	public static final char C_UNDERLINE = CharUtil.UNDERLINE;
	public static final char C_COMMA = CharUtil.COMMA;
	public static final char C_DELIM_START = CharUtil.DELIM_START;
	public static final char C_DELIM_END = CharUtil.DELIM_END;
	public static final char C_BRACKET_START = CharUtil.BRACKET_START;
	public static final char C_BRACKET_END = CharUtil.BRACKET_END;
	public static final char C_COLON = CharUtil.COLON;

	public static final String SPACE = " ";
	public static final String TAB = "	";
	public static final String DOT = ".";
	public static final String DOUBLE_DOT = "..";
	public static final String SLASH = "/";
	public static final String BACKSLASH = "\\";
	public static final String EMPTY = "";
	public static final String CR = "\r";
	public static final String LF = "\n";
	public static final String CRLF = "\r\n";
	public static final String UNDERLINE = "_";
	public static final String DASHED = "-";
	public static final String COMMA = ",";
	public static final String DELIM_START = "{";
	public static final String DELIM_END = "}";
	public static final String BRACKET_START = "[";
	public static final String BRACKET_END = "]";
	public static final String COLON = ":";

	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp;";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_APOS = "&apos;";
	public static final String HTML_LT = "&lt;";
	public static final String HTML_GT = "&gt;";

	public static final String EMPTY_JSON = "{}";

	private StrUtil() {
		throw new AssertionError();
	}

	// ------------------------------------------------------------------------ Blank
	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为不可见字符（如空格）<br>
	 * 3、不为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}



	// ------------------------------------------------------------------------ Empty
	/**
	 * 字符串是否为空，空的定义如下:<br>
	 * 1、为null <br>
	 * 2、为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}


	/**
	 * 字符串是否为非空白 空白的定义如下： <br>
	 * 1、不为null <br>
	 * 2、不为""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(String str) {
		return false == isEmpty(str);
	}

	/**
	 * 当给定字符串为null时，转换为Empty
	 * 
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String nullToEmpty(String str) {
		return nullToDefault(str, EMPTY);
	}

	/**
	 * 如果字符串是<code>null</code>，则返回指定默认字符串，否则返回字符串本身。
	 * 
	 * <pre>
	 * nullToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * nullToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;&quot;
	 * nullToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * nullToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str 要转换的字符串
	 * @param defaultStr 默认字符串
	 * 
	 * @return 字符串本身或指定的默认字符串
	 */
	public static String nullToDefault(String str, String defaultStr) {
		return (str == null) ? defaultStr : str.toString();
	}

	/**
	 * 如果字符串是<code>null</code>或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。
	 * 
	 * <pre>
	 * emptyToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * emptyToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * emptyToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;  &quot;
	 * emptyToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str 要转换的字符串
	 * @param defaultStr 默认字符串
	 * 
	 * @return 字符串本身或指定的默认字符串
	 * @since 4.1.0
	 */
	public static String emptyToDefault(String str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str.toString();
	}

	/**
	 * 是否包含空字符串
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(String... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (String str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否全部为空字符串
	 *
	 * @param strs 字符串列表
	 * @return 是否全部为空字符串
	 */
	public static boolean isAllEmpty(String... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (String str : strs) {
			if (isNotEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 如果字符串是<code>null</code>或者&quot;&quot;或者空白，则返回指定默认字符串，否则返回字符串本身。
	 * 
	 * <pre>
	 * emptyToDefault(null, &quot;default&quot;)  = &quot;default&quot;
	 * emptyToDefault(&quot;&quot;, &quot;default&quot;)    = &quot;default&quot;
	 * emptyToDefault(&quot;  &quot;, &quot;default&quot;)  = &quot;default&quot;
	 * emptyToDefault(&quot;bat&quot;, &quot;default&quot;) = &quot;bat&quot;
	 * </pre>
	 * 
	 * @param str 要转换的字符串
	 * @param defaultStr 默认字符串
	 * 
	 * @return 字符串本身或指定的默认字符串
	 * @since 4.1.0
	 */
	public static String blankToDefault(String str, String defaultStr) {
		return isBlank(str) ? defaultStr : str.toString();
	}

	/**
	 * 当给定字符串为空字符串时，转换为<code>null</code>
	 * 
	 * @param str 被转换的字符串
	 * @return 转换后的字符串
	 */
	public static String emptyToNull(String str) {
		return isEmpty(str) ? null : str.toString();
	}

	/**
	 * 检查字符串是否为null、“null”、“undefined”
	 * 
	 * @param str 被检查的字符串
	 * @return 是否为null、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isNullOrUndefined(String str) {
		if (null == str) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、“”、“null”、“undefined”
	 * 
	 * @param str 被检查的字符串
	 * @return 是否为null、“”、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isEmptyOrUndefined(String str) {
		if (isEmpty(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、空白串、“null”、“undefined”
	 * 
	 * @param str 被检查的字符串
	 * @return 是否为null、空白串、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isBlankOrUndefined(String str) {
		if (isBlank(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 是否为“null”、“undefined”，不做空指针检查
	 * 
	 * @param str 字符串
	 * @return 是否为“null”、“undefined”
	 */
	private static boolean isNullOrUndefinedStr(String str) {
		String strString = str.toString().trim();
		return "null".equals(strString) || "undefined".equals(strString);
	}

	// ------------------------------------------------------------------------ Trim
	/**
	 * 除去字符串头尾部的空白，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>NumberUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * trim(null)          = null
	 * trim(&quot;&quot;)            = &quot;&quot;
	 * trim(&quot;     &quot;)       = &quot;&quot;
	 * trim(&quot;abc&quot;)         = &quot;abc&quot;
	 * trim(&quot;    abc    &quot;) = &quot;abc&quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去头尾空白的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(String str) {
		return (null == str) ? null : trim(str, 0);
	}

	/**
	 * 给定字符串数组全部做去首尾空格
	 * 
	 * @param strs 字符串数组
	 */
	public static void trim(String[] strs) {
		if (null == strs) {
			return;
		}
		String str;
		for (int i = 0; i < strs.length; i++) {
			str = strs[i];
			if (null != str) {
				strs[i] = str.trim();
			}
		}
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，返回<code>""</code>。
	 *
	 * <pre>
	 * StrUtil.trimToEmpty(null)          = ""
	 * StrUtil.trimToEmpty("")            = ""
	 * StrUtil.trimToEmpty("     ")       = ""
	 * StrUtil.trimToEmpty("abc")         = "abc"
	 * StrUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为null返回""
	 * @since 3.1.1
	 */
	public static String trimToEmpty(String str) {
		return str == null ? EMPTY : trim(str);
	}

	/**
	 * 除去字符串头尾部的空白，如果字符串是{@code null}，返回<code>""</code>。
	 *
	 * <pre>
	 * StrUtil.trimToNull(null)          = null
	 * StrUtil.trimToNull("")            = null
	 * StrUtil.trimToNull("     ")       = null
	 * StrUtil.trimToNull("abc")         = "abc"
	 * StrUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 去除两边空白符后的字符串, 如果为空返回null
	 * @since 3.2.1
	 */
	public static String trimToNull(String str) {
		final String trimStr = trim(str);
		return EMPTY.equals(trimStr) ? null : trimStr;
	}

	/**
	 * 除去字符串头部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 * 
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>CharUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 * 
	 * <pre>
	 * trimStart(null)         = null
	 * trimStart(&quot;&quot;)           = &quot;&quot;
	 * trimStart(&quot;abc&quot;)        = &quot;abc&quot;
	 * trimStart(&quot;  abc&quot;)      = &quot;abc&quot;
	 * trimStart(&quot;abc  &quot;)      = &quot;abc  &quot;
	 * trimStart(&quot; abc &quot;)      = &quot;abc &quot;
	 * </pre>
	 * 
	 * @param str 要处理的字符串
	 * 
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
	 */
	public static String trimStart(String str) {
		return trim(str, -1);
	}

	/**
	 * 除去字符串尾部的空白，如果字符串是<code>null</code>，则返回<code>null</code>。
	 *
	 * <p>
	 * 注意，和<code>String.trim</code>不同，此方法使用<code>CharUtil.isBlankChar</code> 来判定空白， 因而可以除去英文字符集之外的其它空白，如中文空格。
	 *
	 * <pre>
	 * trimEnd(null)       = null
	 * trimEnd(&quot;&quot;)         = &quot;&quot;
	 * trimEnd(&quot;abc&quot;)      = &quot;abc&quot;
	 * trimEnd(&quot;  abc&quot;)    = &quot;  abc&quot;
	 * trimEnd(&quot;abc  &quot;)    = &quot;abc&quot;
	 * trimEnd(&quot; abc &quot;)    = &quot; abc&quot;
	 * </pre>
	 *
	 * @param str 要处理的字符串
	 *
	 * @return 除去空白的字符串，如果原字串为<code>null</code>或结果字符串为<code>""</code>，则返回 <code>null</code>
	 */
	public static String trimEnd(String str) {
		return trim(str, 1);
	}

	/**
	 * 除去字符串头尾部的空白符，如果字符串是<code>null</code>，依然返回<code>null</code>。
	 *
	 * @param str 要处理的字符串
	 * @param mode <code>-1</code>表示trimStart，<code>0</code>表示trim全部， <code>1</code>表示trimEnd
	 *
	 * @return 除去指定字符后的的字符串，如果原字串为<code>null</code>，则返回<code>null</code>
	 */
	public static String trim(String str, int mode) {
		if (str == null) {
			return null;
		}

		int length = str.length();
		int start = 0;
		int end = length;

		// 扫描字符串头部
		if (mode <= 0) {
			while ((start < end) && (CharUtil.isBlankChar(str.charAt(start)))) {
				start++;
			}
		}

		// 扫描字符串尾部
		if (mode >= 0) {
			while ((start < end) && (CharUtil.isBlankChar(str.charAt(end - 1)))) {
				end--;
			}
		}

		if ((start > 0) || (end < length)) {
			return str.toString().substring(start, end);
		}

		return str.toString();
	}

	/**
	 * 字符串是否以给定字符开始
	 * 
	 * @param str 字符串
	 * @param c 字符
	 * @return 是否开始
	 */
	public static boolean startWith(String str, char c) {
		return c == str.charAt(0);
	}

	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(String str, String prefix, boolean isIgnoreCase) {
		if (null == str || null == prefix) {
			if (null == str && null == prefix) {
				return true;
			}
			return false;
		}

		if (isIgnoreCase) {
			return str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
		} else {
			return str.toString().startsWith(prefix.toString());
		}
	}

	/**
	 * 是否以指定字符串开头
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(String str, String prefix) {
		return startWith(str, prefix, false);
	}

	/**
	 * 是否以指定字符串开头，忽略大小写
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWithIgnoreCase(String str, String prefix) {
		return startWith(str, prefix, true);
	}

	/**
	 * 字符串是否以给定字符结尾
	 * 
	 * @param str 字符串
	 * @param c 字符
	 * @return 是否结尾
	 */
	public static boolean endWith(String str, char c) {
		return c == str.charAt(str.length() - 1);
	}

	/**
	 * 是否以指定字符串结尾<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(String str, String suffix, boolean isIgnoreCase) {
		if (null == str || null == suffix) {
			if (null == str && null == suffix) {
				return true;
			}
			return false;
		}

		if (isIgnoreCase) {
			return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
		} else {
			return str.toString().endsWith(suffix.toString());
		}
	}

	/**
	 * 是否以指定字符串结尾
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(String str, String suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * 是否以指定字符串结尾，忽略大小写
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWithIgnoreCase(String str, String suffix) {
		return endWith(str, suffix, true);
	}


	/**
	 * 指定字符是否在字符串中出现过
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @return 是否包含
	 * @since 3.1.2
	 */
	public static boolean contains(String str, char searchChar) {
		return indexOf(str, searchChar) > -1;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
	 * 
	 * @param str 指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 * @since 3.2.0
	 */
	public static boolean containsAny(String str, String... testStrs) {
		return null != getContainsStr(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符列表中的任意一个字符
	 *
	 * @param str 指定字符串
	 * @param testChars 需要检查的字符数组
	 * @return 是否包含任意一个字符
	 * @since 4.1.11
	 */
	public static boolean containsAny(String str, char... testChars) {
		if (false == isEmpty(str)) {
			int len = str.length();
			for (int i = 0; i < len; i++) {
				if (ArrayUtil.contains(testChars, str.charAt(i))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 检查指定字符串中是否只包含给定的字符
	 *
	 * @param str 字符串
	 * @param testChars 检查的字符
	 * @return 字符串含有非检查的字符，返回false
	 * @since 4.4.1
	 */
	public static boolean containsOnly(String str, char... testChars) {
		if (false == isEmpty(str)) {
			int len = str.length();
			for (int i = 0; i < len; i++) {
				if (false == ArrayUtil.contains(testChars, str.charAt(i))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 给定字符串是否包含空白符（空白符包括空格、制表符、全角空格和不间断空格）<br>
	 * 如果给定字符串为null或者""，则返回false
	 * 
	 * @param str 字符串
	 * @return 是否包含空白符
	 * @since 4.0.8
	 */
	public static boolean containsBlank(String str) {
		if (null == str) {
			return false;
		}
		final int length = str.length();
		if (0 == length) {
			return false;
		}

		for (int i = 0; i < length; i += 1) {
			if (CharUtil.isBlankChar(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串
	 * 
	 * @param str 指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 * @since 3.2.0
	 */
	public static String getContainsStr(String str, String... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (String checkStr : testStrs) {
			if (str.toString().contains(checkStr)) {
				return checkStr.toString();
			}
		}
		return null;
	}

	/**
	 * 是否包含特定字符，忽略大小写，如果给定两个参数都为<code>null</code>，返回true
	 * 
	 * @param str 被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return 是否包含
	 */
	public static boolean containsIgnoreCase(String str, String testStr) {
		if (null == str) {
			// 如果被监测字符串和
			return null == testStr;
		}
		return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串<br>
	 * 忽略大小写
	 * 
	 * @param str 指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 是否包含任意一个字符串
	 * @since 3.2.0
	 */
	public static boolean containsAnyIgnoreCase(String str, String... testStrs) {
		return null != getContainsStrIgnoreCase(str, testStrs);
	}

	/**
	 * 查找指定字符串是否包含指定字符串列表中的任意一个字符串，如果包含返回找到的第一个字符串<br>
	 * 忽略大小写
	 * 
	 * @param str 指定字符串
	 * @param testStrs 需要检查的字符串数组
	 * @return 被包含的第一个字符串
	 * @since 3.2.0
	 */
	public static String getContainsStrIgnoreCase(String str, String... testStrs) {
		if (isEmpty(str) || ArrayUtil.isEmpty(testStrs)) {
			return null;
		}
		for (String testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	/**
	 * 获得set或get方法对应的标准属性名<br>
	 * 例如：setName 返回 name
	 * 
	 * @param getOrSetMethodName Get或Set方法名
	 * @return 如果是set或get方法名，返回field， 否则null
	 */
	public static String getGeneralField(String getOrSetMethodName) {
		final String getOrSetMethodNameStr = getOrSetMethodName.toString();
		if (getOrSetMethodNameStr.startsWith("get") || getOrSetMethodNameStr.startsWith("set")) {
			return removePreAndLowerFirst(getOrSetMethodName, 3);
		}
		return null;
	}

	/**
	 * 生成set方法名<br>
	 * 例如：name 返回 setName
	 * 
	 * @param fieldName 属性名
	 * @return setXxx
	 */
	public static String genSetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "set");
	}

	/**
	 * 生成get方法名
	 * 
	 * @param fieldName 属性名
	 * @return getXxx
	 */
	public static String genGetter(String fieldName) {
		return upperFirstAndAddPre(fieldName, "get");
	}

	/**
	 * 移除字符串中所有给定字符串<br>
	 * 例：removeAll("aa-bb-cc-dd", "-") =》 aabbccdd
	 * 
	 * @param str 字符串
	 * @param strToRemove 被移除的字符串
	 * @return 移除后的字符串
	 */
	public static String removeAll(String str, String strToRemove) {
		if (isEmpty(str)) {
			return str(str);
		}
		return str.toString().replace(strToRemove, EMPTY);
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, preLength=3 =》 return name
	 * 
	 * @param str 被处理的字符串
	 * @param preLength 去掉的长度
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(String str, int preLength) {
		if (str == null) {
			return null;
		}
		if (str.length() > preLength) {
			char first = Character.toLowerCase(str.charAt(preLength));
			if (str.length() > preLength + 1) {
				return first + str.toString().substring(preLength + 1);
			}
			return String.valueOf(first);
		} else {
			return str.toString();
		}
	}

	/**
	 * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br>
	 * 例如：str=setName, prefix=set =》 return name
	 * 
	 * @param str 被处理的字符串
	 * @param prefix 前缀
	 * @return 处理后的字符串，不符合规范返回null
	 */
	public static String removePreAndLowerFirst(String str, String prefix) {
		return lowerFirst(removePrefix(str, prefix));
	}

	/**
	 * 原字符串首字母大写并在其首部添加指定字符串 例如：str=name, preString=get =》 return getName
	 * 
	 * @param str 被处理的字符串
	 * @param preString 添加的首部
	 * @return 处理后的字符串
	 */
	public static String upperFirstAndAddPre(String str, String preString) {
		if (str == null || preString == null) {
			return null;
		}
		return preString + upperFirst(str);
	}

	/**
	 * 大写首字母<br>
	 * 例如：str = name, return Name
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String upperFirst(String str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isLowerCase(firstChar)) {
				return Character.toUpperCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	/**
	 * 小写首字母<br>
	 * 例如：str = Name, return name
	 * 
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String lowerFirst(String str) {
		if (null == str) {
			return null;
		}
		if (str.length() > 0) {
			char firstChar = str.charAt(0);
			if (Character.isUpperCase(firstChar)) {
				return Character.toLowerCase(firstChar) + subSuf(str, 1);
			}
		}
		return str.toString();
	}

	/**
	 * 获取前缀名
	 * @param str
	 * @return
	 */
	public static String getPrefix(String str) {
		return str.substring(0, str.lastIndexOf("."));
	}

	/**
	 * 获取后缀名
	 * @param str
	 * @return
	 */
	public static String getSuffix(String str) {
		return str.substring(str.lastIndexOf(".") + 1);
	}

	/**
	 * 去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(String str, String prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 忽略大小写去掉指定前缀
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 prefix， 返回原字符串
	 */
	public static String removePrefixIgnoreCase(String str, String prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().startsWith(prefix.toString().toLowerCase())) {
			return subSuf(str2, prefix.length());// 截取后半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffix(String str, String suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return subPre(str2, str2.length() - suffix.length());// 截取前半段
		}
		return str2;
	}

	/**
	 * 去掉指定后缀，并小写首字母
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSufAndLowerFirst(String str, String suffix) {
		return lowerFirst(removeSuffix(str, suffix));
	}

	/**
	 * 忽略大小写去掉指定后缀
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
	 */
	public static String removeSuffixIgnoreCase(String str, String suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		if (str2.toLowerCase().endsWith(suffix.toString().toLowerCase())) {
			return subPre(str2, str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * 去除两边的指定字符串
	 * 
	 * @param str 被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String strip(String str, String prefixOrSuffix) {
		if (equals(str, prefixOrSuffix)) {
			// 对于去除相同字符的情况单独处理
			return EMPTY;
		}
		return strip(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串
	 * 
	 * @param str 被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String strip(String str, String prefix, String suffix) {
		if (isEmpty(str)) {
			return str(str);
		}

		int from = 0;
		int to = str.length();

		String str2 = str.toString();
		if (startWith(str2, prefix)) {
			from = prefix.length();
		}
		if (endWith(str2, suffix)) {
			to -= suffix.length();
		}

		return str2.substring(Math.min(from, to), Math.max(from, to));
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 * 
	 * @param str 被处理的字符串
	 * @param prefixOrSuffix 前缀或后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(String str, String prefixOrSuffix) {
		return stripIgnoreCase(str, prefixOrSuffix, prefixOrSuffix);
	}

	/**
	 * 去除两边的指定字符串，忽略大小写
	 * 
	 * @param str 被处理的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 处理后的字符串
	 * @since 3.1.2
	 */
	public static String stripIgnoreCase(String str, String prefix, String suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		int from = 0;
		int to = str.length();

		String str2 = str.toString();
		if (startWithIgnoreCase(str2, prefix)) {
			from = prefix.length();
		}
		if (endWithIgnoreCase(str2, suffix)) {
			to -= suffix.length();
		}
		return str2.substring(from, to);
	}

	/**
	 * 如果给定字符串不是以prefix开头的，在开头补充 prefix
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return 补充后的字符串
	 */
	public static String addPrefixIfNot(String str, String prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str(str);
		}

		final String str2 = str.toString();
		final String prefix2 = prefix.toString();
		if (false == str2.startsWith(prefix2)) {
			return prefix2.concat(str2);
		}
		return str2;
	}

	/**
	 * 如果给定字符串不是以suffix结尾的，在尾部补充 suffix
	 * 
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return 补充后的字符串
	 */
	public static String addSuffixIfNot(String str, String suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str(str);
		}

		final String str2 = str.toString();
		final String suffix2 = suffix.toString();
		if (false == str2.endsWith(suffix2)) {
			return str2.concat(suffix2);
		}
		return str2;
	}

	/**
	 * 清理空白字符
	 * 
	 * @param str 被清理的字符串
	 * @return 清理后的字符串
	 */
	public static String cleanBlank(String str) {
		if (str == null) {
			return null;
		}

		int len = str.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (false == CharUtil.isBlankChar(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 =》 c <br>
	 * abcdefgh 2 -3 =》 cde <br>
	 *
	 * @param str String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex 结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(String str, int fromIndex, int toIndex) {
		if (isEmpty(str)) {
			return str(str);
		}
		int len = str.length();

		if (fromIndex < 0) {
			fromIndex = len + fromIndex;
			if (fromIndex < 0) {
				fromIndex = 0;
			}
		} else if (fromIndex > len) {
			fromIndex = len;
		}

		if (toIndex < 0) {
			toIndex = len + toIndex;
			if (toIndex < 0) {
				toIndex = len;
			}
		} else if (toIndex > len) {
			toIndex = len;
		}

		if (toIndex < fromIndex) {
			int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}

		if (fromIndex == toIndex) {
			return EMPTY;
		}

		return str.toString().substring(fromIndex, toIndex);
	}

	/**
	 * 切割指定位置之前部分的字符串
	 * 
	 * @param string 字符串
	 * @param toIndex 切割到的位置（不包括）
	 * @return 切割后的剩余的前半部分字符串
	 */
	public static String subPre(String string, int toIndex) {
		return sub(string, 0, toIndex);
	}

	/**
	 * 切割指定位置之后部分的字符串
	 * 
	 * @param string 字符串
	 * @param fromIndex 切割开始的位置（包括）
	 * @return 切割后后剩余的后半部分字符串
	 */
	public static String subSuf(String string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	/**
	 * 切割指定长度的后部分的字符串
	 * 
	 * <pre>
	 * StrUtil.subSufByLength("abcde", 3)      =    "cde"
	 * StrUtil.subSufByLength("abcde", 0)      =    ""
	 * StrUtil.subSufByLength("abcde", -5)     =    ""
	 * StrUtil.subSufByLength("abcde", -1)     =    ""
	 * StrUtil.subSufByLength("abcde", 5)       =    "abcde"
	 * StrUtil.subSufByLength("abcde", 10)     =    "abcde"
	 * StrUtil.subSufByLength(null, 3)               =    null
	 * </pre>
	 * 
	 * @param string 字符串
	 * @param length 切割长度
	 * @return 切割后后剩余的后半部分字符串
	 * @since 4.0.1
	 */
	public static String subSufByLength(String string, int length) {
		if (isEmpty(string)) {
			return null;
		}
		if (length <= 0) {
			return EMPTY;
		}
		return sub(string, -length, string.length());
	}

	/**
	 * 截取字符串,从指定位置开始,截取指定长度的字符串<br>
	 * author weibaohui
	 * 
	 * @param input 原始字符串
	 * @param fromIndex 开始的index,包括
	 * @param length 要截取的长度
	 * @return 截取后的字符串
	 */
	public static String subWithLength(String input, int fromIndex, int length) {
		return sub(input, fromIndex, fromIndex + length);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串为空串""，则返回空串，如果分隔字符串未找到，返回原字符串，举例如下：
	 * 
	 * <pre>
	 * StrUtil.subBefore(null, *)      = null
	 * StrUtil.subBefore("", *)        = ""
	 * StrUtil.subBefore("abc", "a")   = ""
	 * StrUtil.subBefore("abcba", "b") = "a"
	 * StrUtil.subBefore("abc", "c")   = "ab"
	 * StrUtil.subBefore("abc", "d")   = "abc"
	 * StrUtil.subBefore("abc", "")    = ""
	 * StrUtil.subBefore("abc", null)  = "abc"
	 * </pre>
	 * 
	 * @param string 被查找的字符串
	 * @param separator 分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subBefore(String string, String separator, boolean isLastSeparator) {
		if (isEmpty(string) || separator == null) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final String sep = separator.toString();
		if (sep.isEmpty()) {
			return EMPTY;
		}
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""）或者分隔字符串为null，返回原字符串<br>
	 * 如果分隔字符串未找到，返回原字符串，举例如下：
	 * 
	 * <pre>
	 * StrUtil.subBefore(null, *)      = null
	 * StrUtil.subBefore("", *)        = ""
	 * StrUtil.subBefore("abc", 'a')   = ""
	 * StrUtil.subBefore("abcba", 'b') = "a"
	 * StrUtil.subBefore("abc", 'c')   = "ab"
	 * StrUtil.subBefore("abc", 'd')   = "abc"
	 * </pre>
	 * 
	 * @param string 被查找的字符串
	 * @param separator 分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subBefore(String string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : string.toString();
		}

		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return str;
		}
		if (0 == pos) {
			return EMPTY;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *)      = null
	 * StrUtil.subAfter("", *)        = ""
	 * StrUtil.subAfter(*, null)      = ""
	 * StrUtil.subAfter("abc", "a")   = "bc"
	 * StrUtil.subAfter("abcba", "b") = "cba"
	 * StrUtil.subAfter("abc", "c")   = ""
	 * StrUtil.subAfter("abc", "d")   = ""
	 * StrUtil.subAfter("abc", "")    = "abc"
	 * </pre>
	 *
	 * @param string 被查找的字符串
	 * @param separator 分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 3.1.1
	 */
	public static String subAfter(String string, String separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : string.toString();
		}
		if (separator == null) {
			return EMPTY;
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (INDEX_NOT_FOUND == pos || (string.length() - 1) == pos) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串<br>
	 * 如果给定的字符串为空串（null或""），返回原字符串<br>
	 * 如果分隔字符串为空串（null或""），则返回空串，如果分隔字符串未找到，返回空串，举例如下：
	 *
	 * <pre>
	 * StrUtil.subAfter(null, *)      = null
	 * StrUtil.subAfter("", *)        = ""
	 * StrUtil.subAfter("abc", 'a')   = "bc"
	 * StrUtil.subAfter("abcba", 'b') = "cba"
	 * StrUtil.subAfter("abc", 'c')   = ""
	 * StrUtil.subAfter("abc", 'd')   = ""
	 * </pre>
	 *
	 * @param string 被查找的字符串
	 * @param separator 分隔字符串（不包括）
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return 切割后的字符串
	 * @since 4.1.15
	 */
	public static String subAfter(String string, char separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : string.toString();
		}
		final String str = string.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
		if (INDEX_NOT_FOUND == pos) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * 
	 * 栗子：
	 * 
	 * <pre>
	 * StrUtil.subBetween("wx[b]yz", "[", "]") = "b"
	 * StrUtil.subBetween(null, *, *)          = null
	 * StrUtil.subBetween(*, null, *)          = null
	 * StrUtil.subBetween(*, *, null)          = null
	 * StrUtil.subBetween("", "", "")          = ""
	 * StrUtil.subBetween("", "", "]")         = null
	 * StrUtil.subBetween("", "[", "]")        = null
	 * StrUtil.subBetween("yabcz", "", "")     = ""
	 * StrUtil.subBetween("yabcz", "y", "z")   = "abc"
	 * StrUtil.subBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * 
	 * @param str 被切割的字符串
	 * @param before 截取开始的字符串标识
	 * @param after 截取到的字符串标识
	 * @return 截取后的字符串
	 * @since 3.1.1
	 */
	public static String subBetween(String str, String before, String after) {
		if (str == null || before == null || after == null) {
			return null;
		}

		final String str2 = str.toString();
		final String before2 = before.toString();
		final String after2 = after.toString();

		final int start = str2.indexOf(before2);
		if (start != INDEX_NOT_FOUND) {
			final int end = str2.indexOf(after2, start + before2.length());
			if (end != INDEX_NOT_FOUND) {
				return str2.substring(start + before2.length(), end);
			}
		}
		return null;
	}

	/**
	 * 截取指定字符串中间部分，不包括标识字符串<br>
	 * 
	 * 栗子：
	 * 
	 * <pre>
	 * StrUtil.subBetween(null, *)            = null
	 * StrUtil.subBetween("", "")             = ""
	 * StrUtil.subBetween("", "tag")          = null
	 * StrUtil.subBetween("tagabctag", null)  = null
	 * StrUtil.subBetween("tagabctag", "")    = ""
	 * StrUtil.subBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 * 
	 * @param str 被切割的字符串
	 * @param beforeAndAfter 截取开始和结束的字符串标识
	 * @return 截取后的字符串
	 * @since 3.1.1
	 */
	public static String subBetween(String str, String beforeAndAfter) {
		return subBetween(str, beforeAndAfter, beforeAndAfter);
	}

	/**
	 * 给定字符串是否被字符包围
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否包围，空串不包围
	 */
	public static boolean isSurround(String str, String prefix, String suffix) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		if (str.length() < (prefix.length() + suffix.length())) {
			return false;
		}

		final String str2 = str.toString();
		return str2.startsWith(prefix.toString()) && str2.endsWith(suffix.toString());
	}

	/**
	 * 给定字符串是否被字符包围
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否包围，空串不包围
	 */
	public static boolean isSurround(String str, char prefix, char suffix) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		if (str.length() < 2) {
			return false;
		}

		return str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix;
	}

	/**
	 * 重复某个字符
	 * 
	 * @param c 被重复的字符
	 * @param count 重复的数目，如果小于等于0则返回""
	 * @return 重复字符字符串
	 */
	public static String repeat(char c, int count) {
		if (count <= 0) {
			return EMPTY;
		}

		char[] result = new char[count];
		for (int i = 0; i < count; i++) {
			result[i] = c;
		}
		return new String(result);
	}

	/**
	 * 重复某个字符串
	 * 
	 * @param str 被重复的字符
	 * @param count 重复的数目
	 * @return 重复字符字符串
	 */
	public static String repeat(String str, int count) {
		if (null == str) {
			return null;
		}
		if (count <= 0) {
			return EMPTY;
		}
		if (count == 1 || str.length() == 0) {
			return str.toString();
		}

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		final char[] array = new char[size];
		str.toString().getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 重复某个字符串到指定长度
	 * 
	 * @param str 被重复的字符
	 * @param padLen 指定长度
	 * @return 重复字符字符串
	 * @since 4.3.2
	 */
	public static String repeatByLength(String str, int padLen) {
		if (null == str) {
			return null;
		}
		if (padLen <= 0) {
			return StrUtil.EMPTY;
		}
		final int strLen = str.length();
		if (strLen == padLen) {
			return str.toString();
		} else if (strLen > padLen) {
			return subPre(str, padLen);
		}

		// 重复，直到达到指定长度
		final char[] padding = new char[padLen];
		for (int i = 0; i < padLen; i++) {
			padding[i] = str.charAt(i % strLen);
		}
		return new String(padding);
	}

	/**
	 * 比较两个字符串（大小写敏感）。
	 * 
	 * <pre>
	 * equals(null, null)   = true
	 * equals(null, &quot;abc&quot;)  = false
	 * equals(&quot;abc&quot;, null)  = false
	 * equals(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equals(&quot;abc&quot;, &quot;ABC&quot;) = false
	 * </pre>
	 * 
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * 
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equals(String str1, String str2) {
		return equals(str1, str2, false);
	}

	/**
	 * 比较两个字符串（大小写不敏感）。
	 * 
	 * <pre>
	 * equalsIgnoreCase(null, null)   = true
	 * equalsIgnoreCase(null, &quot;abc&quot;)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, null)  = false
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;abc&quot;) = true
	 * equalsIgnoreCase(&quot;abc&quot;, &quot;ABC&quot;) = true
	 * </pre>
	 * 
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * 
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 */
	public static boolean equalsIgnoreCase(String str1, String str2) {
		return equals(str1, str2, true);
	}

	/**
	 * 比较两个字符串是否相等。
	 * 
	 * @param str1 要比较的字符串1
	 * @param str2 要比较的字符串2
	 * @param ignoreCase 是否忽略大小写
	 * @return 如果两个字符串相同，或者都是<code>null</code>，则返回<code>true</code>
	 * @since 3.2.0
	 */
	public static boolean equals(String str1, String str2, boolean ignoreCase) {
		if (null == str1) {
			// 只有两个都为null才判断相等
			return str2 == null;
		}
		if (null == str2) {
			// 字符串2空，字符串1非空，直接false
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.equals(str2);
		}
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同（忽略大小写），相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 * 
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAnyIgnoreCase(String str1, String... strs) {
		return equalsAny(str1, true, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 * 
	 * @param str1 给定需要检查的字符串
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAny(String str1, String... strs) {
		return equalsAny(str1, false, strs);
	}

	/**
	 * 给定字符串是否与提供的中任一字符串相同，相同则返回{@code true}，没有相同的返回{@code false}<br>
	 * 如果参与比对的字符串列表为空，返回{@code false}
	 * 
	 * @param str1 给定需要检查的字符串
	 * @param ignoreCase 是否忽略大小写
	 * @param strs 需要参与比对的字符串列表
	 * @return 是否相同
	 * @since 4.3.2
	 */
	public static boolean equalsAny(String str1, boolean ignoreCase, String... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return false;
		}

		for (String str : strs) {
			if (equals(str1, str, ignoreCase)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 有序的格式化文本，使用{number}做为占位符<br>
	 * 例：<br>
	 * 通常使用：format("this is {0} for {1}", "a", "b") =》 this is a for b<br>
	 * 
	 * @param pattern 文本格式
	 * @param arguments 参数
	 * @return 格式化后的文本
	 */
	public static String indexedFormat(String pattern, Object... arguments) {
		return MessageFormat.format(pattern.toString(), arguments);
	}

	/**
	 * 编码字符串<br>
	 * 使用系统默认编码
	 * 
	 * @param str 字符串
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(String str) {
		return bytes(str, Charset.defaultCharset());
	}

	/**
	 * 编码字符串
	 * 
	 * @param str 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(String str, String charset) {
		return bytes(str, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 编码字符串
	 * 
	 * @param str 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 编码后的字节码
	 */
	public static byte[] bytes(String str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 *
	 * @param obj 对象
	 * @return 字符串
	 */
	public static String utf8Str(Object obj) {
		return str(obj, CharsetUtil.CHARSET_UTF_8);
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 * 
	 * @param obj 对象
	 * @param charsetName 字符集
	 * @return 字符串
	 */
	public static String str(Object obj, String charsetName) {
		return str(obj, Charset.forName(charsetName));
	}

	/**
	 * 将对象转为字符串<br>
	 * 1、Byte数组和ByteBuffer会被转换为对应字符串的数组 2、对象数组会调用Arrays.toString方法
	 * 
	 * @param obj 对象
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Object obj, Charset charset) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof String) {
			return (String) obj;
		} else if (obj instanceof byte[]) {
			return str((byte[]) obj, charset);
		} else if (obj instanceof Byte[]) {
			return str((Byte[]) obj, charset);
		} else if (obj instanceof ByteBuffer) {
			return str((ByteBuffer) obj, charset);
		} else if (ArrayUtil.isArray(obj)) {
			return ArrayUtil.toString(obj);
		}

		return obj.toString();
	}

	/**
	 * 将byte数组转为字符串
	 * 
	 * @param bytes byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(byte[] bytes, String charset) {
		return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 解码字节码
	 * 
	 * @param data 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * 将Byte数组转为字符串
	 * 
	 * @param bytes byte数组
	 * @param charset 字符集
	 * @return 字符串
	 */
	public static String str(Byte[] bytes, String charset) {
		return str(bytes, isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * 解码字节码
	 * 
	 * @param data 字符串
	 * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
	 * @return 解码后的字符串
	 */
	public static String str(Byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		byte[] bytes = new byte[data.length];
		Byte dataByte;
		for (int i = 0; i < data.length; i++) {
			dataByte = data[i];
			bytes[i] = (null == dataByte) ? -1 : dataByte.byteValue();
		}

		return str(bytes, charset);
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 * 
	 * @param data 数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, String charset) {
		if (data == null) {
			return null;
		}

		return str(data, Charset.forName(charset));
	}

	/**
	 * 将编码的byteBuffer数据转换为字符串
	 * 
	 * @param data 数据
	 * @param charset 字符集，如果为空使用当前系统字符集
	 * @return 字符串
	 */
	public static String str(ByteBuffer data, Charset charset) {
		if (null == charset) {
			charset = Charset.defaultCharset();
		}
		return charset.decode(data).toString();
	}

	/**
	 * {@link String} 转为字符串，null安全
	 * 
	 * @param cs {@link String}
	 * @return 字符串
	 */
	public static String str(String cs) {
		return null == cs ? null : cs.toString();
	}

	/**
	 * 调用对象的toString方法，null会返回“null”
	 * 
	 * @param obj 对象
	 * @return 字符串
	 * @since 4.1.3
	 */
	public static String toString(Object obj) {
		return null == obj ? "null" : obj.toString();
	}

	/**
	 * 字符串转换为byteBuffer
	 * 
	 * @param str 字符串
	 * @param charset 编码
	 * @return byteBuffer
	 */
	public static ByteBuffer byteBuffer(String str, String charset) {
		return ByteBuffer.wrap(bytes(str, charset));
	}

	/**
	 * 将驼峰式命名的字符串转换为下划线方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 * 例如：
	 * 
	 * <pre>
	 * HelloWorld=》hello_world
	 * Hello_World=》hello_world
	 * HelloWorld_test=》hello_world_test
	 * </pre>
	 *
	 * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
	 * @return 转换后下划线方式命名的字符串
	 */
	public static String toUnderlineCase(String str) {
		return toSymbolCase(str, CharUtil.UNDERLINE);
	}

	/**
	 * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 *
	 * @param str 转换前的驼峰式命名的字符串，也可以为符号连接形式
	 * @param symbol 连接符
	 * @return 转换后符号连接方式命名的字符串
	 * @since 4.0.10
	 */
	public static String toSymbolCase(String str, char symbol) {
		if (str == null) {
			return null;
		}

		final int length = str.length();
		final StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
			if (Character.isUpperCase(c)) {
				// 遇到大写字母处理
				final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;
				if (null != preChar && Character.isUpperCase(preChar)) {
					// 前一个字符为大写，则按照一个词对待
					sb.append(c);
				} else if (null != nextChar && Character.isUpperCase(nextChar)) {
					// 后一个为大写字母，按照一个词对待
					if (null != preChar && symbol != preChar) {
						// 前一个是非大写时按照新词对待，加连接符
						sb.append(symbol);
					}
					sb.append(c);
				} else {
					// 前后都为非大写按照新词对待
					if (null != preChar && symbol != preChar) {
						// 前一个非连接符，补充连接符
						sb.append(symbol);
					}
					sb.append(Character.toLowerCase(c));
				}
			} else {
				if (sb.length() > 0 && Character.isUpperCase(sb.charAt(sb.length() - 1)) && symbol != c) {
					// 当结果中前一个字母为大写，当前为小写，说明此字符为新词开始（连接符也表示新词）
					sb.append(symbol);
				}
				// 小写或符号
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 例如：hello_world=》helloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(String name) {
		if (null == name) {
			return null;
		}

		String name2 = name.toString();
		if (name2.contains(UNDERLINE)) {
			final StringBuilder sb = new StringBuilder(name2.length());
			boolean upperCase = false;
			for (int i = 0; i < name2.length(); i++) {
				char c = name2.charAt(i);

				if (c == CharUtil.UNDERLINE) {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(Character.toLowerCase(c));
				}
			}
			return sb.toString();
		} else {
			return name2;
		}
	}

	/**
	 * 包装指定字符串<br>
	 * 当前缀和后缀一致时使用此方法
	 * 
	 * @param str 被包装的字符串
	 * @param prefixAndSuffix 前缀和后缀
	 * @return 包装后的字符串
	 * @since 3.1.0
	 */
	public static String wrap(String str, String prefixAndSuffix) {
		return wrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 包装指定字符串
	 * 
	 * @param str 被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrap(String str, String prefix, String suffix) {
		return nullToEmpty(prefix).concat(nullToEmpty(str)).concat(nullToEmpty(suffix));
	}

	/**
	 * 包装多个字符串
	 * 
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs 多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAll(String prefixAndSuffix, String... strs) {
		return wrapAll(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串
	 * 
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs 多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAll(String prefix, String suffix, String... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrap(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 包装指定字符串，如果前缀或后缀已经包含对应的字符串，则不再包装
	 * 
	 * @param str 被包装的字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 包装后的字符串
	 */
	public static String wrapIfMissing(String str, String prefix, String suffix) {
		int len = 0;
		if (isNotEmpty(str)) {
			len += str.length();
		}
		if (isNotEmpty(prefix)) {
			len += str.length();
		}
		if (isNotEmpty(suffix)) {
			len += str.length();
		}
		StringBuilder sb = new StringBuilder(len);
		if (isNotEmpty(prefix) && false == startWith(str, prefix)) {
			sb.append(prefix);
		}
		if (isNotEmpty(str)) {
			sb.append(str);
		}
		if (isNotEmpty(suffix) && false == endWith(str, suffix)) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	/**
	 * 包装多个字符串，如果已经包装，则不再包装
	 * 
	 * @param prefixAndSuffix 前缀和后缀
	 * @param strs 多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAllIfMissing(String prefixAndSuffix, String... strs) {
		return wrapAllIfMissing(prefixAndSuffix, prefixAndSuffix, strs);
	}

	/**
	 * 包装多个字符串，如果已经包装，则不再包装
	 * 
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @param strs 多个字符串
	 * @return 包装的字符串数组
	 * @since 4.0.7
	 */
	public static String[] wrapAllIfMissing(String prefix, String suffix, String... strs) {
		final String[] results = new String[strs.length];
		for (int i = 0; i < strs.length; i++) {
			results[i] = wrapIfMissing(strs[i], prefix, suffix);
		}
		return results;
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 * 
	 * @param str 字符串
	 * @param prefix 前置字符串
	 * @param suffix 后置字符串
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(String str, String prefix, String suffix) {
		if (isWrap(str, prefix, suffix)) {
			return sub(str, prefix.length(), str.length() - suffix.length());
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 * 
	 * @param str 字符串
	 * @param prefix 前置字符
	 * @param suffix 后置字符
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(String str, char prefix, char suffix) {
		if (isEmpty(str)) {
			return str(str);
		}
		if (str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix) {
			return sub(str, 1, str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * 去掉字符包装，如果未被包装则返回原字符串
	 * 
	 * @param str 字符串
	 * @param prefixAndSuffix 前置和后置字符
	 * @return 去掉包装字符的字符串
	 * @since 4.0.1
	 */
	public static String unWrap(String str, char prefixAndSuffix) {
		return unWrap(str, prefixAndSuffix, prefixAndSuffix);
	}

	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str 字符串
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, String prefix, String suffix) {
		if (ArrayUtil.hasNull(str, prefix, suffix)) {
			return false;
		}
		final String str2 = str.toString();
		return str2.startsWith(prefix) && str2.endsWith(suffix);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str 字符串
	 * @param wrapper 包装字符串
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, String wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被同一字符包装（前后都有这些字符串）
	 * 
	 * @param str 字符串
	 * @param wrapper 包装字符
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, char wrapper) {
		return isWrap(str, wrapper, wrapper);
	}

	/**
	 * 指定字符串是否被包装
	 * 
	 * @param str 字符串
	 * @param prefixChar 前缀
	 * @param suffixChar 后缀
	 * @return 是否被包装
	 */
	public static boolean isWrap(String str, char prefixChar, char suffixChar) {
		if (null == str) {
			return false;
		}

		return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
	}

	/**
	 * 补充字符串以满足最小长度
	 * 
	 * <pre>
	 * StrUtil.padPre(null, *, *);//null
	 * StrUtil.padPre("1", 3, "ABC");//"AB1"
	 * StrUtil.padPre("123", 2, "ABC");//"12"
	 * </pre>
	 * 
	 * @param str 字符串
	 * @param minLength 最小长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(String str, int minLength, String padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == minLength) {
			return str.toString();
		} else if (strLen > minLength) {
			return subPre(str, minLength);
		}

		return repeatByLength(padStr, minLength - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度
	 * 
	 * <pre>
	 * StrUtil.padPre(null, *, *);//null
	 * StrUtil.padPre("1", 3, '0');//"001"
	 * StrUtil.padPre("123", 2, '0');//"12"
	 * </pre>
	 * 
	 * @param str 字符串
	 * @param minLength 最小长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padPre(String str, int minLength, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == minLength) {
			return str.toString();
		} else if (strLen > minLength) {
			return subPre(str, minLength);
		}

		return repeat(padChar, minLength - strLen).concat(str.toString());
	}

	/**
	 * 补充字符串以满足最小长度
	 * 
	 * <pre>
	 * StrUtil.padAfter(null, *, *);//null
	 * StrUtil.padAfter("1", 3, '0');//"100"
	 * StrUtil.padAfter("123", 2, '0');//"23"
	 * </pre>
	 * 
	 * @param str 字符串，如果为<code>null</code>，按照空串处理
	 * @param minLength 最小长度
	 * @param padChar 补充的字符
	 * @return 补充后的字符串
	 */
	public static String padAfter(String str, int minLength, char padChar) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == minLength) {
			return str.toString();
		} else if (strLen > minLength) {
			return sub(str, strLen - minLength, strLen);
		}

		return str.toString().concat(repeat(padChar, minLength - strLen));
	}

	/**
	 * 补充字符串以满足最小长度
	 * 
	 * <pre>
	 * StrUtil.padAfter(null, *, *);//null
	 * StrUtil.padAfter("1", 3, "ABC");//"1AB"
	 * StrUtil.padAfter("123", 2, "ABC");//"23"
	 * </pre>
	 * 
	 * @param str 字符串，如果为<code>null</code>，按照空串处理
	 * @param minLength 最小长度
	 * @param padStr 补充的字符
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String padAfter(String str, int minLength, String padStr) {
		if (null == str) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == minLength) {
			return str.toString();
		} else if (strLen > minLength) {
			return subSufByLength(str, minLength);
		}

		return str.toString().concat(repeatByLength(padStr, minLength - strLen));
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 * 
	 * <pre>
	 * StrUtil.center(null, *)   = null
	 * StrUtil.center("", 4)     = "    "
	 * StrUtil.center("ab", -1)  = "ab"
	 * StrUtil.center("ab", 4)   = " ab "
	 * StrUtil.center("abcd", 2) = "abcd"
	 * StrUtil.center("a", 4)    = " a  "
	 * </pre>
	 *
	 * @param str 字符串
	 * @param size 指定长度
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String center(String str, final int size) {
		return center(str, size, CharUtil.SPACE);
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 * 
	 * <pre>
	 * StrUtil.center(null, *, *)     = null
	 * StrUtil.center("", 4, ' ')     = "    "
	 * StrUtil.center("ab", -1, ' ')  = "ab"
	 * StrUtil.center("ab", 4, ' ')   = " ab "
	 * StrUtil.center("abcd", 2, ' ') = "abcd"
	 * StrUtil.center("a", 4, ' ')    = " a  "
	 * StrUtil.center("a", 4, 'y')   = "yayy"
	 * StrUtil.center("abc", 7, ' ')   = "  abc  "
	 * </pre>
	 *
	 * @param str 字符串
	 * @param size 指定长度
	 * @param padChar 两边补充的字符
	 * @return 补充后的字符串
	 * @since 4.3.2
	 */
	public static String center(String str, final int size, char padChar) {
		if (str == null || size <= 0) {
			return str(str);
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padChar);
		str = padAfter(str, size, padChar);
		return str.toString();
	}

	/**
	 * 居中字符串，两边补充指定字符串，如果指定长度小于字符串，则返回原字符串
	 * 
	 * <pre>
	 * StrUtil.center(null, *, *)     = null
	 * StrUtil.center("", 4, " ")     = "    "
	 * StrUtil.center("ab", -1, " ")  = "ab"
	 * StrUtil.center("ab", 4, " ")   = " ab "
	 * StrUtil.center("abcd", 2, " ") = "abcd"
	 * StrUtil.center("a", 4, " ")    = " a  "
	 * StrUtil.center("a", 4, "yz")   = "yayz"
	 * StrUtil.center("abc", 7, null) = "  abc  "
	 * StrUtil.center("abc", 7, "")   = "  abc  "
	 * </pre>
	 *
	 * @param str 字符串
	 * @param size 指定长度
	 * @param padStr 两边补充的字符串
	 * @return 补充后的字符串
	 */
	public static String center(String str, final int size, String padStr) {
		if (str == null || size <= 0) {
			return str(str);
		}
		if (isEmpty(padStr)) {
			padStr = SPACE;
		}
		final int strLen = str.length();
		final int pads = size - strLen;
		if (pads <= 0) {
			return str.toString();
		}
		str = padPre(str, strLen + pads / 2, padStr);
		str = padAfter(str, size, padStr);
		return str.toString();
	}

	/**
	 * 统计指定内容中包含指定字符串的数量<br>
	 * 参数为 {@code null} 或者 "" 返回 {@code 0}.
	 *
	 * <pre>
	 * StrUtil.count(null, *)       = 0
	 * StrUtil.count("", *)         = 0
	 * StrUtil.count("abba", null)  = 0
	 * StrUtil.count("abba", "")    = 0
	 * StrUtil.count("abba", "a")   = 2
	 * StrUtil.count("abba", "ab")  = 1
	 * StrUtil.count("abba", "xxx") = 0
	 * </pre>
	 *
	 * @param content 被查找的字符串
	 * @param strForSearch 需要查找的字符串
	 * @return 查找到的个数
	 */
	public static int count(String content, String strForSearch) {
		if (hasEmpty(content, strForSearch) || strForSearch.length() > content.length()) {
			return 0;
		}

		int count = 0;
		int idx = 0;
		final String content2 = content.toString();
		final String strForSearch2 = strForSearch.toString();
		while ((idx = content2.indexOf(strForSearch2, idx)) > -1) {
			count++;
			idx += strForSearch.length();
		}
		return count;
	}

	/**
	 * 统计指定内容中包含指定字符的数量
	 * 
	 * @param content 内容
	 * @param charForSearch 被统计的字符
	 * @return 包含数量
	 */
	public static int count(String content, char charForSearch) {
		int count = 0;
		if (isEmpty(content)) {
			return 0;
		}
		int contentLength = content.length();
		for (int i = 0; i < contentLength; i++) {
			if (charForSearch == content.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 比较两个字符串，用于排序
	 * 
	 * <pre>
	 * StrUtil.compare(null, null, *)     = 0
	 * StrUtil.compare(null , "a", true)  &lt; 0
	 * StrUtil.compare(null , "a", false) &gt; 0
	 * StrUtil.compare("a", null, true)   &gt; 0
	 * StrUtil.compare("a", null, false)  &lt; 0
	 * StrUtil.compare("abc", "abc", *)   = 0
	 * StrUtil.compare("a", "b", *)       &lt; 0
	 * StrUtil.compare("b", "a", *)       &gt; 0
	 * StrUtil.compare("a", "B", *)       &gt; 0
	 * StrUtil.compare("ab", "abc", *)    &lt; 0
	 * </pre>
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compare(final String str1, final String str2, final boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareTo(str2.toString());
	}

	/**
	 * 比较两个字符串，用于排序，大小写不敏感
	 * 
	 * <pre>
	 * StrUtil.compareIgnoreCase(null, null, *)     = 0
	 * StrUtil.compareIgnoreCase(null , "a", true)  &lt; 0
	 * StrUtil.compareIgnoreCase(null , "a", false) &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, true)   &gt; 0
	 * StrUtil.compareIgnoreCase("a", null, false)  &lt; 0
	 * StrUtil.compareIgnoreCase("abc", "abc", *)   = 0
	 * StrUtil.compareIgnoreCase("abc", "ABC", *)   = 0
	 * StrUtil.compareIgnoreCase("a", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("b", "a", *)       &gt; 0
	 * StrUtil.compareIgnoreCase("a", "B", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("A", "b", *)       &lt; 0
	 * StrUtil.compareIgnoreCase("ab", "abc", *)    &lt; 0
	 * </pre>
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param nullIsLess {@code null} 值是否排在前（null是否小于非空值）
	 * @return 排序值。负数：str1 &lt; str2，正数：str1 &gt; str2, 0：str1 == str2
	 */
	public static int compareIgnoreCase(String str1, String str2, boolean nullIsLess) {
		if (str1 == str2) {
			return 0;
		}
		if (str1 == null) {
			return nullIsLess ? -1 : 1;
		}
		if (str2 == null) {
			return nullIsLess ? 1 : -1;
		}
		return str1.toString().compareToIgnoreCase(str2.toString());
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @return 位置
	 */
	public static int indexOf(final String str, char searchChar) {
		return indexOf(str, searchChar, 0);
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @param start 起始位置，如果小于0，从0开始查找
	 * @return 位置
	 */
	public static int indexOf(final String str, char searchChar, int start) {
		if (str instanceof String) {
			return ((String) str).indexOf(searchChar, start);
		} else {
			return indexOf(str, searchChar, start, -1);
		}
	}

	/**
	 * 指定范围内查找指定字符
	 * 
	 * @param str 字符串
	 * @param searchChar 被查找的字符
	 * @param start 起始位置，如果小于0，从0开始查找
	 * @param end 终止位置，如果超过str.length()则默认查找到字符串末尾
	 * @return 位置
	 */
	public static int indexOf(final String str, char searchChar, int start, int end) {
		final int len = str.length();
		if (start < 0 || start > len) {
			start = 0;
		}
		if (end > len || end < 0) {
			end = len;
		}
		for (int i = start; i < end; i++) {
			if (str.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 指定范围内查找字符串，忽略大小写<br>
	 * 
	 * <pre>
	 * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
	 * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
	 * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final String str, final String searchStr) {
		return indexOfIgnoreCase(str, searchStr, 0);
	}

	/**
	 * 指定范围内查找字符串
	 * 
	 * <pre>
	 * StrUtil.indexOfIgnoreCase(null, *, *)          = -1
	 * StrUtil.indexOfIgnoreCase(*, null, *)          = -1
	 * StrUtil.indexOfIgnoreCase("", "", 0)           = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0)  = 0
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0)  = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0) = 1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3)  = 5
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9)  = -1
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1) = 2
	 * StrUtil.indexOfIgnoreCase("aabaabaa", "", 2)   = 2
	 * StrUtil.indexOfIgnoreCase("abc", "", 9)        = -1
	 * </pre>
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOfIgnoreCase(final String str, final String searchStr, int fromIndex) {
		return indexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 指定范围内查找字符串
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int indexOf(final String str, String searchStr, int fromIndex, boolean ignoreCase) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}

		final int endLimit = str.length() - searchStr.length() + 1;
		if (fromIndex > endLimit) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return fromIndex;
		}

		if (false == ignoreCase) {
			// 不忽略大小写调用JDK方法
			return str.toString().indexOf(searchStr.toString(), fromIndex);
		}

		for (int i = fromIndex; i < endLimit; i++) {
			if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 指定范围内查找字符串，忽略大小写
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(final String str, final String searchStr) {
		return lastIndexOfIgnoreCase(str, searchStr, str.length());
	}

	/**
	 * 指定范围内查找字符串，忽略大小写<br>
	 * fromIndex 为搜索起始位置，从后往前计数
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置，从后往前计数
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOfIgnoreCase(final String str, final String searchStr, int fromIndex) {
		return lastIndexOf(str, searchStr, fromIndex, true);
	}

	/**
	 * 指定范围内查找字符串<br>
	 * fromIndex 为搜索起始位置，从后往前计数
	 * 
	 * @param str 字符串
	 * @param searchStr 需要查找位置的字符串
	 * @param fromIndex 起始位置，从后往前计数
	 * @param ignoreCase 是否忽略大小写
	 * @return 位置
	 * @since 3.2.1
	 */
	public static int lastIndexOf(final String str, final String searchStr, int fromIndex, boolean ignoreCase) {
		if (str == null || searchStr == null) {
			return INDEX_NOT_FOUND;
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		fromIndex = Math.min(fromIndex, str.length());

		if (searchStr.length() == 0) {
			return fromIndex;
		}

		if (false == ignoreCase) {
			// 不忽略大小写调用JDK方法
			return str.toString().lastIndexOf(searchStr.toString(), fromIndex);
		}

		for (int i = fromIndex; i > 0; i--) {
			if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	/**
	 * 返回字符串 searchStr 在字符串 str 中第 ordinal 次出现的位置。<br>
	 * 如果 str=null 或 searchStr=null 或 ordinal<=0 则返回-1<br>
	 * 此方法来自：Apache-Commons-Lang
	 * 
	 * 栗子（*代表任意字符）：
	 * 
	 * <pre>
	 * StrUtil.ordinalIndexOf(null, *, *)          = -1
	 * StrUtil.ordinalIndexOf(*, null, *)          = -1
	 * StrUtil.ordinalIndexOf("", "", *)           = 0
	 * StrUtil.ordinalIndexOf("aabaabaa", "a", 1)  = 0
	 * StrUtil.ordinalIndexOf("aabaabaa", "a", 2)  = 1
	 * StrUtil.ordinalIndexOf("aabaabaa", "b", 1)  = 2
	 * StrUtil.ordinalIndexOf("aabaabaa", "b", 2)  = 5
	 * StrUtil.ordinalIndexOf("aabaabaa", "ab", 1) = 1
	 * StrUtil.ordinalIndexOf("aabaabaa", "ab", 2) = 4
	 * StrUtil.ordinalIndexOf("aabaabaa", "", 1)   = 0
	 * StrUtil.ordinalIndexOf("aabaabaa", "", 2)   = 0
	 * </pre>
	 *
	 * @param str 被检查的字符串，可以为null
	 * @param searchStr 被查找的字符串，可以为null
	 * @param ordinal 第几次出现的位置
	 * @return 查找到的位置
	 * @since 3.2.3
	 */
	public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return INDEX_NOT_FOUND;
		}
		if (searchStr.length() == 0) {
			return 0;
		}
		int found = 0;
		int index = INDEX_NOT_FOUND;
		do {
			index = str.indexOf(searchStr, index + 1);
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}

	// ------------------------------------------------------------------------------------------------------------------ Append and prepend
	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 不忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final String str, final String suffix, final String... suffixes) {
		return appendIfMissing(str, suffix, false, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串<br>
	 * 忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissingIgnoreCase(final String str, final String suffix, final String... suffixes) {
		return appendIfMissing(str, suffix, true, suffixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为结尾，则在尾部添加结尾字符串
	 *
	 * @param str 被检查的字符串
	 * @param suffix 需要添加到结尾的字符串
	 * @param ignoreCase 检查结尾时是否忽略大小写
	 * @param suffixes 需要额外检查的结尾字符串，如果以这些中的一个为结尾，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String appendIfMissing(final String str, final String suffix, final boolean ignoreCase, final String... suffixes) {
		if (str == null || isEmpty(suffix) || endWith(str, suffix, ignoreCase)) {
			return str(str);
		}
		if (suffixes != null && suffixes.length > 0) {
			for (final String s : suffixes) {
				if (endWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return str.toString().concat(suffix.toString());
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
	 * 不忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final String str, final String prefix, final String... prefixes) {
		return prependIfMissing(str, prefix, false, prefixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串<br>
	 * 忽略大小写
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissingIgnoreCase(final String str, final String prefix, final String... prefixes) {
		return prependIfMissing(str, prefix, true, prefixes);
	}

	/**
	 * 如果给定字符串不是以给定的一个或多个字符串为开头，则在首部添加起始字符串
	 *
	 * @param str 被检查的字符串
	 * @param prefix 需要添加到首部的字符串
	 * @param ignoreCase 检查结尾时是否忽略大小写
	 * @param prefixes 需要额外检查的首部字符串，如果以这些中的一个为起始，则不再添加
	 *
	 * @return 如果已经结尾，返回原字符串，否则返回添加结尾的字符串
	 * @since 3.0.7
	 */
	public static String prependIfMissing(final String str, final String prefix, final boolean ignoreCase, final String... prefixes) {
		if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase)) {
			return str(str);
		}
		if (prefixes != null && prefixes.length > 0) {
			for (final String s : prefixes) {
				if (startWith(str, s, ignoreCase)) {
					return str.toString();
				}
			}
		}
		return prefix.toString().concat(str.toString());
	}

	/**
	 * 反转字符串<br>
	 * 例如：abcd =》dcba
	 * 
	 * @param str 被反转的字符串
	 * @return 反转后的字符串
	 * @since 3.0.9
	 */
	public static String reverse(String str) {
		return new String(ArrayUtil.reverse(str.toCharArray()));
	}

	/**
	 * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
	 * 字符填充于字符串前
	 * 
	 * @param str 被填充的字符串
	 * @param filledChar 填充的字符
	 * @param len 填充长度
	 * @return 填充后的字符串
	 * @since 3.1.2
	 */
	public static String fillBefore(String str, char filledChar, int len) {
		return fill(str, filledChar, len, true);
	}

	/**
	 * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串<br>
	 * 字符填充于字符串后
	 * 
	 * @param str 被填充的字符串
	 * @param filledChar 填充的字符
	 * @param len 填充长度
	 * @return 填充后的字符串
	 * @since 3.1.2
	 */
	public static String fillAfter(String str, char filledChar, int len) {
		return fill(str, filledChar, len, false);
	}

	/**
	 * 将已有字符串填充为规定长度，如果已有字符串超过这个长度则返回这个字符串
	 * 
	 * @param str 被填充的字符串
	 * @param filledChar 填充的字符
	 * @param len 填充长度
	 * @param isPre 是否填充在前
	 * @return 填充后的字符串
	 * @since 3.1.2
	 */
	public static String fill(String str, char filledChar, int len, boolean isPre) {
		final int strLen = str.length();
		if (strLen > len) {
			return str;
		}

		String filledStr = StrUtil.repeat(filledChar, len - strLen);
		return isPre ? filledStr.concat(str) : str.concat(filledStr);
	}

	/**
	 * 截取两个字符串的不同部分（长度一致），判断截取的子串是否相同<br>
	 * 任意一个字符串为null返回false
	 * 
	 * @param str1 第一个字符串
	 * @param start1 第一个字符串开始的位置
	 * @param str2 第二个字符串
	 * @param start2 第二个字符串开始的位置
	 * @param length 截取长度
	 * @param ignoreCase 是否忽略大小写
	 * @return 子串是否相同
	 * @since 3.2.1
	 */
	public static boolean isSubEquals(String str1, int start1, String str2, int start2, int length, boolean ignoreCase) {
		if (null == str1 || null == str2) {
			return false;
		}

		return str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length);
	}

	/**
	 * 替换指定字符串的指定区间内字符为固定字符
	 * 
	 * @param str 字符串
	 * @param startInclude 开始位置（包含）
	 * @param endExclude 结束位置（不包含）
	 * @param replacedChar 被替换的字符
	 * @return 替换后的字符串
	 * @since 3.2.1
	 */
	public static String replace(String str, int startInclude, int endExclude, char replacedChar) {
		if (isEmpty(str)) {
			return str(str);
		}
		final int strLength = str.length();
		if (startInclude > strLength) {
			return str(str);
		}
		if (endExclude > strLength) {
			endExclude = strLength;
		}
		if (startInclude > endExclude) {
			// 如果起始位置大于结束位置，不替换
			return str(str);
		}

		final char[] chars = new char[strLength];
		for (int i = 0; i < strLength; i++) {
			if (i >= startInclude && i < endExclude) {
				chars[i] = replacedChar;
			} else {
				chars[i] = str.charAt(i);
			}
		}
		return new String(chars);
	}

	/**
	 * 替换指定字符串的指定区间内字符为"*"
	 *
	 * @param str 字符串
	 * @param startInclude 开始位置（包含）
	 * @param endExclude 结束位置（不包含）
	 * @return 替换后的字符串
	 * @since 4.1.14
	 */
	public static String hide(String str, int startInclude, int endExclude) {
		return replace(str, startInclude, endExclude, '*');
	}

	/**
	 * 字符串指定位置的字符是否与给定字符相同<br>
	 * 如果字符串为null，返回false<br>
	 * 如果给定的位置大于字符串长度，返回false<br>
	 * 如果给定的位置小于0，返回false
	 * 
	 * @param str 字符串
	 * @param position 位置
	 * @param c 需要对比的字符
	 * @return 字符串指定位置的字符是否与给定字符相同
	 * @since 3.3.1
	 */
	public static boolean equalsCharAt(String str, int position, char c) {
		if (null == str || position < 0) {
			return false;
		}
		return str.length() > position && c == str.charAt(position);
	}

	/**
	 * 给定字符串数组的总长度<br>
	 * null字符长度定义为0
	 * 
	 * @param strs 字符串数组
	 * @return 总长度
	 * @since 4.0.1
	 */
	public static int totalLength(String... strs) {
		int totalLength = 0;
		for (int i = 0; i < strs.length; i++) {
			totalLength += (null == strs[i] ? 0 : strs[i].length());
		}
		return totalLength;
	}

	/**
	 * 给定字符串中的字母是否全部为大写，判断依据如下：
	 * 
	 * <pre>
	 * 1. 大写字母包括A-Z
	 * 2. 其它非字母的Unicode符都算作大写
	 * </pre>
	 * 
	 * @param str 被检查的字符串
	 * @return 是否全部为大写
	 * @since 4.2.2
	 */
	public static boolean isUpperCase(String str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isLowerCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 给定字符串中的字母是否全部为小写，判断依据如下：
	 * 
	 * <pre>
	 * 1. 小写字母包括a-z
	 * 2. 其它非字母的Unicode符都算作小写
	 * </pre>
	 * 
	 * @param str 被检查的字符串
	 * @return 是否全部为小写
	 * @since 4.2.2
	 */
	public static boolean isLowerCase(String str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取字符串的长度，如果为null返回0
	 *
	 * @param cs a 字符串
	 * @return 字符串的长度，如果为null返回0
	 * @since 4.3.2
	 */
	public static int length(final String cs) {
		return cs == null ? 0 : cs.length();
	}

	/**
	 * 切换给定字符串中的大小写。大写转小写，小写转大写。
	 *
	 * <pre>
	 * StrUtil.swapCase(null)                 = null
	 * StrUtil.swapCase("")                   = ""
	 * StrUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 交换后的字符串
	 * @since 4.3.2
	 */
	public static String swapCase(final String str) {
		if (isEmpty(str)) {
			return str;
		}

		final char[] buffer = str.toCharArray();

		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (Character.isUpperCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}

	/**
	 * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上appendString
	 */
	public static String checkLength(String string, int maxLength, String appendString) {
		if (string.length() > maxLength) {
			string = string.substring(0, maxLength);
			if (appendString != null) {
				string += appendString;
			}
		}
		return string;
	}


	/**
	 * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上…
	 */
	public static String checkLength(String string, int maxLength) {
		return checkLength(string, maxLength, "…");
	}


	/**
	 * 删除Html标签
	 *
	 * @param inputString
	 * @return
	 */
	public static String htmlRemoveTag(String inputString) {
		if (inputString == null)
			return null;
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;
		try {
			//定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
			//定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签
			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签
			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签
			textStr = htmlStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 切分字符串<br>
	 * a#b#c =》 [a,b,c] <br>
	 * a##b#c =》 [a,"",b,c]
	 *
	 * @param str 被切分的字符串
	 * @param separator 分隔符字符
	 * @return 切分后的集合
	 */
	public static List<String> splitToList(String str, String separator) {
		String[] arr = str.split(separator);
		List<String> list = new ArrayList<>();
		for (String s : arr) {
			list.add(s);
		}
		return list;
	}

	public static List<String> splitToList(String str, char separator) {
		return splitToList(str, Character.valueOf(separator));
	}


	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is \{} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param params 参数值
	 * @return 格式化后的文本
	 */
	public static String format(String template, Object... params) {
		if (null == template) {
			return null;
		}
		if (ArrayUtil.isEmpty(params) || isBlank(template)) {
			return template.toString();
		}
		return StrFormatter.format(template.toString(), params);
	}

	/**
	 * 下划线转驼峰
	 *
	 * @param line       源字符串
	 * @param smallCamel 大小驼峰,是否为小驼峰
	 * @return 转换后的字符串
	 */
	public static String underlineToCamel(String line, boolean smallCamel) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0)) : Character.toUpperCase(word.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * 驼峰转下划线
	 *
	 * @param line 源字符串
	 * @return 转换后的字符串
	 */
	public static String camelToUnderline(String line) {
		if (line == null || "".equals(line)) {
			return "";
		}
		line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(word.toUpperCase());
			sb.append(matcher.end() == line.length() ? "" : "_");
		}
		return sb.toString();
	}

}
