package com.huch.common.io;

import com.huch.common.collection.CollUtil;
import com.huch.common.io.File.LineSeparator;
import com.huch.common.util.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.*;
import java.util.UUID;
import java.util.regex.Pattern;

public class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    public final static String FILE_EXTENSION_SEPARATOR = ".";
    /** URI类型：file */
    public static final String URI_TYPE_FILE = "file";
    /** 类Unix路径分隔符 */
    private static final char UNIX_SEPARATOR = '/';
    /** Windows路径分隔符 */
    private static final char WINDOWS_SEPARATOR = '\\';
    /** Windows下文件名中的无效字符 */
    private static Pattern FILE_NAME_INVALID_PATTERN_WIN = Pattern.compile("[\\\\/:*?\"<>|]");

    /** Class文件扩展名 */
    public static final String CLASS_EXT = ".class";
    /** Jar文件扩展名 */
    public static final String JAR_FILE_EXT = ".jar";
    /** 在Jar中的路径jar的扩展名形式 */
    public static final String JAR_PATH_EXT = ".jar!";
    /** 当Path为文件形式时, path会加入一个表示文件的前缀 */
   public static final String PATH_FILE_PRE = "file:";


    /**
     * 生成随机文件名
     * @return
     */
    public static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 是否为Windows环境
     *
     * @return 是否为Windows环境
     * @since 3.0.9
     */
    public static boolean isWindows() {
        return WINDOWS_SEPARATOR == File.separatorChar;
    }

    /**
     * 是否为Unix环境
     * @return
     */
    public static boolean isUnix(){
        return UNIX_SEPARATOR == File.separatorChar;
    }

    /**
     * 文件是否为空<br>
     * 目录：里面没有文件时为空
     * 文件：文件大小为0时为空
     *
     * @param file 文件
     * @return 是否为空，当提供非目录时，返回false
     */
    public static boolean isEmpty(File file) {
        if (null == file) {
            return true;
        }

        if (file.isDirectory()) {
            String[] subFiles = file.list();
            if (null == subFiles || subFiles.length == 0) {
                return true;
            }
        } else if (file.isFile()) {
            return file.length() <= 0;
        }

        return false;
    }

    /**
     * 列出目录文件<br>
     * 给定的绝对路径不能是压缩包中的路径
     *
     * @param path 目录绝对路径或者相对路径
     * @return 文件列表（包含目录）
     */
    public static File[] ls(String path) {
        if (path == null) {
            return null;
        }

        File file = new File(path);
        if (file.isDirectory()) {
            return file.listFiles();
        }
        throw new RuntimeException(path + " is not directory!");
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
     * 如果提供file为文件，直接返回过滤结果
     *
     * @param path 当前遍历文件或目录的路径
     * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
     * @return 文件列表
     * @since 3.2.0
     */
    public static List<File> loopFiles(String path, FileFilter fileFilter) {
        return loopFiles(new File(path), fileFilter);
    }

    /**
     * 递归遍历目录以及子目录中的所有文件<br>
	 * 如果提供file为文件，直接返回过滤结果
	 *
     * @param file 当前遍历文件或目录
	 * @param fileFilter 文件过滤规则对象，选择要保留的文件，只对文件有效，不过滤目录
	 * @return 文件列表
	 */
    public static List<File> loopFiles(File file, FileFilter fileFilter) {
        List<File> fileList = new ArrayList();
        if (null == file) {
            return fileList;
        } else if (false == file.exists()) {
            return fileList;
        }

        if (file.isDirectory()) {
            final File[] subFiles = file.listFiles();
            if (ArrayUtil.isNotEmpty(subFiles)) {
                for (File tmp : subFiles) {
                    fileList.addAll(loopFiles(tmp, fileFilter));
                }
            }
        } else {
            if (null == fileFilter || fileFilter.accept(file)) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param path 当前遍历文件或目录的路径
     * @return 文件列表
     * @since 3.2.0
     */
    public static List<File> loopFiles(String path) {
        return loopFiles(new File(path));
    }

    /**
     * 递归遍历目录以及子目录中的所有文件
     *
     * @param file 当前遍历文件
     * @return 文件列表
     */
    public static List<File> loopFiles(File file) {
        return loopFiles(file, null);
    }

    /**
     * 获得相对子路径
     *
     * 栗子：
     *
     * <pre>
     * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
     * </pre>
     *
     * @param rootDir 绝对父路径
     * @param file 文件
     * @return 相对子路径
     */
    public static String subPath(String rootDir, File file) {
        try {
            return subPath(rootDir, file.getCanonicalPath());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 获得相对子路径，忽略大小写
     *
     * 栗子：
     *
     * <pre>
     * dirPath: d:/aaa/bbb    filePath: d:/aaa/bbb/ccc     =》    ccc
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/ccc.txt     =》    ccc.txt
     * dirPath: d:/Aaa/bbb    filePath: d:/aaa/bbb/     =》    ""
     * </pre>
     *
     * @param dirPath 父路径
     * @param filePath 文件路径
     * @return 相对子路径
     */
    public static String subPath(String dirPath, String filePath) {
        if (StrUtil.isNotEmpty(dirPath) && StrUtil.isNotEmpty(filePath)) {

            dirPath = StrUtil.removeSuffix(normalize(dirPath), "/");
            filePath = normalize(filePath);

            final String result = StrUtil.removePrefixIgnoreCase(filePath, dirPath);
            return StrUtil.removePrefix(result, "/");
        }
        return filePath;
    }

    /**
     * 读文件内容
     *
     * @param filePath    路径
     * @param charsetName 编码类型
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        return readFile(file, charsetName);
    }



    public static File file(String path) {
        if(StrUtil.isBlank(path)) throw new NullPointerException("File path is bland!");
        return new File(normalize(path));
    }

    /**
     * 创建File对象<br>
     * @param parent 父目录
     * @param path 文件路径
     * @return File
     */
    public static File file(String parent, String path) {
        return new File(parent, path);
    }

    /**
     * 创建File对象<br>
     * @param parent 父目录
     * @param path 文件路径
     * @return File
     */
    public static File file(File parent, String path) {
        return new File(parent, path);
    }

    /**
     * 返回主文件名
     *
     * @param file 文件
     * @return 主文件名
     */
    public static String mainName(File file) {
        if (file.isDirectory()) {
            return file.getName();
        }
        return mainName(file.getName());
    }

    /**
     * 返回主文件名, 文件名没有后缀
     *
     * @param fileName 完整文件名
     * @return 主文件名
     */
    public static String mainName(String fileName) {
        if (null == fileName) {
            return fileName;
        }
        int len = fileName.length();
        if (0 == len) {
            return fileName;
        }
        if (CharUtil.isFileSeparator(fileName.charAt(len - 1))) {
            len--;
        }

        int begin = 0;
        int end = len;
        char c;
        for (int i = len - 1; i > -1; i--) {
            c = fileName.charAt(i);
            if (len == end && CharUtil.DOT == c) {
                // 查找最后一个文件名和扩展名的分隔符：.
                end = i;
            }
            if (0 == begin || begin > end) {
                if (CharUtil.isFileSeparator(c)) {
                    // 查找最后一个路径分隔符（/或者\），如果这个分隔符在.之后，则继续查找，否则结束
                    begin = i + 1;
                    break;
                }
            }
        }

        return fileName.substring(begin, end);
    }



    /**
     * 创建文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        final File dir = file(dirPath);
        return mkdir(dir);
    }

    /**
     * 创建文件夹，会递归自动创建其不存在的父文件夹，如果存在直接返回此文件夹<br>
     * 此方法不对File对象类型做判断，如果File不存在，无法判断其类型
     *
     * @param dir 目录
     * @return 创建的目录
     */
    public static File mkdir(File dir) {
        if (dir == null) {
            return null;
        }
        if (false == dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 读取文件内容
     * @param file
     * @param charsetName
     * @return
     */
    public static StringBuilder readFile(File file, String charsetName) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader( new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(reader);
        }
    }


    /**
     * 读取文件内容, 默认字符集UTF-8
     *
     * @param file 文件
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(File file) throws IORuntimeException {
        return readString(file, Charset.forName("UTF-8"));
    }

    /**
     * 读取文件内容, 默认字符集UTF-8
     *
     * @param path 文件路径
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(String path) throws IORuntimeException {
        return readString(path, Charset.forName("UTF-8"));
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(File file, String charsetName) throws IORuntimeException {
        return readString(file, Charset.forName(charsetName));
    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(File file, Charset charset) throws IORuntimeException {
        return readFile(file, charset.name()).toString();
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @param charsetName 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(String path, String charsetName) throws IORuntimeException {
        return readString(new File(path), charsetName);
    }

    /**
     * 读取文件内容
     *
     * @param path 文件路径
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(String path, Charset charset) throws IORuntimeException {
        return readString(new File(path), charset);
    }

    /**
     * 读取文件内容
     *
     * @param url 文件URL
     * @param charset 字符集
     * @return 内容
     * @throws IORuntimeException IO异常
     */
    public static String readString(URL url, String charset) throws IORuntimeException {
        if (url == null) {
            throw new NullPointerException("Empty url provided!");
        }

        InputStream in = null;
        try {
            in = url.openStream();
            return IOUtil.read(in, charset);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IOUtil.close(in);
        }
    }

    /**
     * 读取文件内容到List集合中
     * @param filePath
     * @return
     */
    public static List<String> readStringToList(String filePath) {
        return readStringToList(filePath, "UTF-8");
    }

    /**
     * 读取文件内容到List集合中
     *
     * @param filePath    路径
     * @param charsetName 字符编码
     * @return 如果文件存在返回null, 如果文件存在返回List
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readStringToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            // 读取一行数据
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(reader);
        }
    }

    /**
     * 写入文件
     * @param filename 文件名
     * @param inputStream
     * @throws IOException
     */
    public static void write(String filename, InputStream inputStream) throws IOException {
        OutputStream os = new FileOutputStream(filename);
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = inputStream.read(buf))) {
            os.write(buf,0,len);
        }
        os.flush();
        os.close();
    }

    /**
     * 分块写入文件
     * @param fileName 文件文
     * @param fileSize 文件总大小
     * @param inputStream 输入流
     * @param blokSize 文件分块大小
     * @param chunks 总分块数
     * @param chunk 当前分块下标, 0开始
     * @throws IOException
     */
    public static void writeWithBlok(String fileName, Long fileSize, InputStream inputStream, Long blokSize, Integer chunks, Integer chunk) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName,"rw");
        randomAccessFile.setLength(fileSize);
        if (chunk == chunks - 1 && chunk != 0) {
            randomAccessFile.seek(chunk * (fileSize - blokSize) / chunk);
        } else {
            randomAccessFile.seek(chunk * blokSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = inputStream.read(buf))) {
            randomAccessFile.write(buf,0,len);
        }
        randomAccessFile.close();
    }

    /**
     * 将Sring写入到文件
     * @param content 内容
     * @param path  路径
     * @param charset 编码格式
     * @param append  是否是追加模式
     * @return
     */
    public static File writeString(String content, String path, String charset,  boolean append){
        File file = file(path);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));;
            writer.write(content);
            writer.flush();
        }catch(IOException e){
            throw new IORuntimeException(e);
        }finally {
            IOUtil.close(writer);
        }
        return file;
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, String path, String charset) throws IORuntimeException {
        return writeString(content, path, charset, false);
    }

    /**
     * 将String写入到文件, 覆盖模式, 默认字符集UTF-8
     * @param content 写入的内容
     * @param path 文件路径
     * @return
     */
    public static File writeString(String content, String path){
        return writeString(content, path, "UTF-8");
    }

    /**
     * 将String写入文件, 覆盖模式, 字符集为UTF-8
     * @param content
     * @param file
     * @return
     */
    public static File writeString(String content, File file){
        return writeString(content, file, "UTF-8");
    }

    /**
     * 将String写入文件，覆盖模式，字符集为UTF-8
     *
     * @param content 写入的内容
     * @param file 文件
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, File file, String charset) throws IORuntimeException {
        return writeString(content, file, charset, false);
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 被写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File writeString(String content, File file, String charset, boolean append){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));;
            writer.write(content);
            writer.flush();
        }catch(IOException e){
            throw new IORuntimeException(e);
        }finally {
            IOUtil.close(writer);
        }
        return file;
    }



    /**
     * 将String写入文件，UTF-8编码追加模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static File appendString(String content, String path) throws IORuntimeException {
        return appendString(content, path, "UTF-8");
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param path 文件路径
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, String path, String charset) {
        return writeString(content, path, charset, true);
    }

    /**
     * 将String写入文件，UTF-8编码追加模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static File appendString(String content, File file) throws IORuntimeException {
        return appendString(content, file, "UTF-8");
    }

    /**
     * 将String写入文件，追加模式
     *
     * @param content 写入的内容
     * @param file 文件
     * @param charset 字符集
     * @return 写入的文件
     * @throws IORuntimeException IO异常
     */
    public static File appendString(String content, File file, String charset) throws IORuntimeException {
        return writeString(content, file, charset, true);
    }

    /**
     * 将列表写入文件，覆盖模式，编码为UTF-8
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 3.2.0
     */
    public static <T> File writeLines(Collection<T> list, String path) throws IORuntimeException {
        return writeLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将列表写入文件，覆盖模式，编码为UTF-8
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 绝对路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 3.2.0
     */
    public static <T> File writeLines(Collection<T> list, File file) throws IORuntimeException {
        return writeLines(list, file, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines(list, path, charset, false);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines(list, path, charset, false);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 4.2.0
     */
    public static <T> File writeLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines(list, file, charset, false);
    }

    /**
     * 将列表写入文件，覆盖模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 4.2.0
     */
    public static <T> File writeLines(Collection<T> list, File file, Charset charset) throws IORuntimeException {
        return writeLines(list, file, charset.toString(), false);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static <T> File appendLines(Collection<T> list, File file) throws IORuntimeException {
        return appendLines(list, file, "UTF-8");
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static <T> File appendLines(Collection<T> list, String path) throws IORuntimeException {
        return appendLines(list, path, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, String path, String charset) throws IORuntimeException {
        return writeLines(list, path, charset, true);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 3.1.2
     */
    public static <T> File appendLines(Collection<T> list, File file, String charset) throws IORuntimeException {
        return writeLines(list, file, charset, true);
    }

    /**
     * 将列表写入文件，追加模式
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 绝对路径
     * @param charset 字符集
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File appendLines(Collection<T> list, String path, Charset charset) throws IORuntimeException {
        return writeLines(list, path, charset, true);
    }

    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, String charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset, isAppend);
    }

    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param path 文件路径
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, String path, Charset charset, boolean isAppend) throws IORuntimeException {
        return writeLines(list, file(path), charset.toString(), isAppend);
    }

    /**
     * 将列表写入文件
     *
     * @param <T> 集合元素类型
     * @param list 列表
     * @param file 文件
     * @param charset 字符集
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static <T> File writeLines(Collection<T> list, File file, String charset, boolean isAppend){
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), charset)))){
            for (T t : list) {
                if (null != t) {
                    writer.print(t.toString());
                    printNewLine(writer, null);
                    writer.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
     *
     * @param map Map
     * @param file 文件
     * @param kvSeparator 键和值之间的分隔符，如果传入null使用默认分隔符" = "
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 4.0.5
     */
    public static File writeUtf8Map(Map<?, ?> map, File file, String kvSeparator, boolean isAppend) throws IORuntimeException {
        return writeMap(map, file, Charset.forName("UTF-8"), kvSeparator, isAppend);
    }

    /**
     * 将Map写入文件，每个键值对为一行，一行中键与值之间使用kvSeparator分隔
     *
     * @param map Map
     * @param file 文件
     * @param charset 字符集编码
     * @param kvSeparator 键和值之间的分隔符，如果传入null使用默认分隔符" = "
     * @param isAppend 是否追加
     * @return 目标文件
     * @throws IORuntimeException IO异常
     * @since 4.0.5
     */
    public static File writeMap(Map<?, ?> map, File file, Charset charset, String kvSeparator, boolean isAppend) throws IORuntimeException {
        if(null == kvSeparator) {
            kvSeparator = " = ";
        }
        try(PrintWriter writer  = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FileUtil.file(file.getAbsolutePath()), isAppend), charset)))) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (null != entry) {
                    writer.print(StrUtil.format("{}{}{}", entry.getKey(), kvSeparator, entry.getValue()));
                    printNewLine(writer, null);
                    writer.flush();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @param path 目标文件
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, String path) throws IORuntimeException {
        return writeBytes(data, file(path));
    }

    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, File dest) throws IORuntimeException {
        return writeBytes(data, dest, 0, data.length, false);
    }

    /**
     * 写入数据到文件
     *
     * @param data 数据
     * @param dest 目标文件
     * @param off 数据开始位置
     * @param len 数据长度
     * @param isAppend 是否追加模式
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeBytes(byte[] data, File dest, int off, int len, boolean isAppend) throws IORuntimeException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(FileUtil.file(dest.getAbsolutePath()), isAppend);
            out.write(data, off, len);
            out.flush();
        }catch(IOException e){
            throw new IORuntimeException(e);
        } finally {
            IOUtil.close(out);
        }
        return dest;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param dest 目标文件
     * @param in 输入流
     * @return dest
     * @throws IORuntimeException IO异常
     */
    public static File writeFromStream(InputStream in, File dest) throws IORuntimeException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file(dest.getAbsolutePath()));
            IOUtil.copy(in, out);
        }catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IOUtil.close(out);
        }
        return dest;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param in 输入流
     * @param fullFilePath 文件绝对路径
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeFromStream(InputStream in, String fullFilePath) throws IORuntimeException {
        return writeFromStream(in, file(fullFilePath));
    }

    /**
     * 将文件写入流中
     *
     * @param file 文件
     * @param out 流
     * @return 目标文件
     * @throws IORuntimeException IO异常
     */
    public static File writeToStream(File file, OutputStream out) throws IORuntimeException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            IOUtil.copy(in, out);
        }catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            IOUtil.close(in);
        }
        return file;
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param fullFilePath 文件绝对路径
     * @param out 输出流
     * @throws IORuntimeException IO异常
     */
    public static void writeToStream(String fullFilePath, OutputStream out) throws IORuntimeException {
        writeToStream(file(fullFilePath), out);
    }



    /**
     * 写文件
     *
     * @param filePath 路径
     * @param content  上下文
     * @param append   是否追加
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (StrUtil.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(fileWriter);
        }
    }


    /**
     * 写数据到文件
     *
     * @param filePath    路径
     * @param contentList 集合
     * @param append      是否追加
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, List<String> contentList, boolean append) {
        if (contentList.size() == 0 || null == contentList) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n");
                }
                fileWriter.write(line);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(fileWriter);
        }
    }


    /**
     * 写数据到文件，会覆盖源文件内容
     *
     * @param filePath 文件路径
     * @param content  写入内容
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, String content) {

        return writeFile(filePath, content, false);
    }


    /**
     * 写数据到文件，会覆盖源文件内容
     *
     * @param filePath    地址
     * @param contentList 集合
     * @return 是否写入成功
     */
    public static boolean writeFile(String filePath, List<String> contentList) {
        return writeFile(filePath, contentList, false);

    }


    /**
     * 写数据到文件，会覆盖源文件内容
     *
     * @param filePath 路径
     * @param stream   输入流
     * @return 返回是否写入成功
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * 写数据到文件
     *
     * @param filePath 路径
     * @param stream   输入流
     * @param append   是否追加，ture追加写入 false覆盖写入
     * @return return true
     * FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * 写数据到文件，会覆盖源文件内容
     *
     * @param file   文件对象
     * @param stream 输入流
     * @return 返回是否写入成功
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);

    }


    /**
     * 写数据到文件
     *
     * @param file   文件
     * @param stream 输入流
     * @param append 是否追加，ture追加写入 false覆盖写入
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            IOUtil.close(o);
            IOUtil.close(stream);
        }
    }


    /**
     * 移动文件
     *
     * @param sourceFilePath 资源路径
     * @param destFilePath   目标路径
     */
    public static void moveFile(String sourceFilePath, String destFilePath) {

        if (StrUtil.isEmpty(sourceFilePath) || StrUtil.isEmpty(destFilePath)) {
            throw new RuntimeException( "Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }


    /**
     * 移动文件
     *
     * @param srcFile  文件对象
     * @param destFile 对象
     */
    public static void moveFile(File srcFile, File destFile) {

        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }


    /**
     * 复制文件
     *
     * @param sourceFilePath 资源路径
     * @param destFilePath   目标路径
     * @return 返回是否成功
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }





    /**
     * @param filePath 文件的路径
     * @return 返回文件的信息
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
                extenPosi) : filePath.substring(filePosi + 1));
    }


    /**
     * 获取路径中的文件名
     * <p>
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath 路径
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }


    /**
     * 获取当前路径所在文件夹名
     * <p>
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath 路径
     * @return file name from path, include suffix
     */
    public static String getFolderName(String filePath) {
        if (StrUtil.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取路径后缀名
     * <p>
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath 路径
     * @return 信息
     */
    public static String getFileExtension(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }


    /**
     * 创建目录
     * @param filePath 路径
     * @return 是否创建成功
     */
    public static boolean makeDirs(String filePath) {

        String folderName = getFolderName(filePath);
        if (StrUtil.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory())
                ? true
                : folder.mkdirs();
    }

    /**
     * 创建目录
     * @param filePath 路径
     * @return 是否创建成功
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * 文件是否存在
     * @param filePath 路径
     * @return 是否存在这个文件
     */
    public static boolean isFileExist(String filePath) {
        if (StrUtil.isBlank(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }


    /**
     * 是否有文件夹
     * @param directoryPath 路径
     * @return 是否有文件夹
     */
    public static boolean isFolderExist(String directoryPath) {
        if (StrUtil.isBlank(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * 删除文件
     * @param path 路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String path) {
        if (StrUtil.isBlank(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }


    /**
     * 获取文件大小
     * @param path 路径
     * @return 返回文件大小
     */
    public static long getFileSize(String path) {
        if (StrUtil.isBlank(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 保存多媒体数据为文件.
     *
     * @param data     多媒体数据
     * @param fileName 保存文件名
     * @return 保存成功或失败
     */
    public static boolean save2File(InputStream data, String fileName) {
        File file = new File(fileName);
        FileOutputStream fos = null;
        try {
            // 文件或目录不存在时,创建目录和文件.
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // 写入数据
            fos = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len;
            while ((len = data.read(b)) > -1) {
                fos.write(b, 0, len);
            }
            fos.close();

            return true;
        } catch (IOException ex) {

            return false;
        }
    }


    /**
     * 读取文件的字节数组.
     *
     * @param file 文件
     * @return 字节数组
     */
    public static byte[] readFileToBytes(File file) {
        // 如果文件不存在,返回空
        if (!file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            // 读取文件内容.
            fis = new FileInputStream(file);
            byte[] arrData = new byte[(int) file.length()];
            fis.read(arrData);
            // 返回
            return arrData;
        } catch (IOException e) {
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {

                }
            }
        }
    }


    /**
     * 读取文本文件内容，以行的形式读取
     *
     * @param filePathAndName 带有完整绝对路径的文件名
     * @return String 返回文本文件的内容
     */
    public static String readFileContent(String filePathAndName) {
        try {
            return readFileContent(filePathAndName, null, null, 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 读取文本文件内容，以行的形式读取
     *
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding        文本文件打开的编码方式 例如 GBK,UTF-8
     * @param sep             分隔符 例如：#，默认为\n;
     * @param bufLen          设置缓冲区大小
     * @return String 返回文本文件的内容
     */
    public static String readFileContent(String filePathAndName, String encoding, String sep, int bufLen) {
        if (filePathAndName == null || filePathAndName.equals("")) {
            return "";
        }
        if (sep == null || sep.equals("")) {
            sep = "\n";
        }
        if (!new File(filePathAndName).exists()) {
            return "";
        }
        StringBuffer str = new StringBuffer("");
        FileInputStream fs = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fs = new FileInputStream(filePathAndName);
            if (encoding == null || encoding.trim().equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding.trim());
            }
            br = new BufferedReader(isr, bufLen);

            String data = "";
            while ((data = br.readLine()) != null) {
                str.append(data).append(sep);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (br != null) br.close();
                if (isr != null) isr.close();
                if (fs != null) fs.close();
            } catch (IOException e) {
            }
        }
        return str.toString();
    }


    /**
     * 根据文件路径，检查文件是否不大于指定大小
     *
     * @param filepath 文件路径
     * @param maxSize  最大
     * @return 是否
     */
    public static boolean checkFileSize(String filepath, int maxSize) {
        File file = new File(filepath);
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        if (file.length() <= maxSize * 1024) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 将字符串以UTF-8编码保存到文件中
     *
     * @param str      保存的字符串
     * @param fileName 文件的名字
     * @return 是否保存成功
     */
    public static boolean saveStrToFile(String str, String fileName) {
        return saveStrToFile(str, fileName, "UTF-8");
    }


    /**
     * 将字符串以charsetName编码保存到文件中
     *
     * @param str         保存的字符串
     * @param fileName    文件的名字
     * @param charsetName 字符串编码
     * @return 是否保存成功
     */
    public static boolean saveStrToFile(String str, String fileName, String charsetName) {
        if (str == null || "".equals(str)) {
            return false;
        }

        FileOutputStream stream = null;
        try {
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            byte[] b = null;
            if (charsetName != null && !"".equals(charsetName)) {
                b = str.getBytes(charsetName);
            } else {
                b = str.getBytes();
            }

            stream = new FileOutputStream(file);
            stream.write(b, 0, b.length);
            stream.flush();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 文件重命名
     *
     * @param oldPath 旧的文件路径
     * @param newPath 新的文件路径
     */
    public static void renameFile(String oldPath, String newPath) {
        try {
            if (!StrUtil.isEmpty(oldPath) && !StrUtil.isEmpty(newPath) && !oldPath.equals(newPath)) {
                File fileOld = new File(oldPath);
                File fileNew = new File(newPath);
                fileOld.renameTo(fileNew);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file 文件
     * @return 字节码
     * @throws IORuntimeException IO异常
     */
    public static byte[] readToByteArray(File file) {
        FileInputStream fis = null;
        byte[] arr = new byte[(int)file.length()];
        try {
            fis = new FileInputStream(file);
            fis.read(arr);
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return arr;
    }

    /**
     * 检查文件
     *
     * @throws IORuntimeException IO异常
     */
    public static void checkFile(File file) throws IORuntimeException {
        if (false == file.exists()) {
            throw new IORuntimeException("File not exist: " + file);
        }
        if (false == file.isFile()) {
            throw new IORuntimeException("Not a file:" + file);
        }
    }

    /**
     * 修复路径<br>
     * 如果原路径尾部有分隔符，则保留为标准分隔符（/），否则不保留
     * <ol>
     * <li>1. 统一用 /</li>
     * <li>2. 多个 / 转换为一个 /</li>
     * <li>3. 去除两边空格</li>
     * <li>4. .. 和 . 转换为绝对路径，当..多于已有路径时，直接返回根路径</li>
     * </ol>
     *
     * 例：
     * <pre>
     * "/foo//" =》 "/foo/"
     * "/foo/./" =》 "/foo/"
     * "/foo/../bar" =》 "/bar"
     * "/foo/../bar/" =》 "/bar/"
     * "/foo/../bar/../baz" =》 "/baz"
     * "/../" =》 "/"
     * "foo/bar/.." =》 "foo"
     * "foo/../bar" =》 "bar"
     * "foo/../../bar" =》 "bar"
     * "//server/foo/../bar" =》 "/server/bar"
     * "//server/../bar" =》 "/bar"
     * "C:\\foo\\..\\bar" =》 "C:/bar"
     * "C:\\..\\bar" =》 "C:/bar"
     * "~/foo/../bar/" =》 "~/bar/"
     * "~/../bar" =》 "bar"
     * </pre>
     *
     * @param path 原路径
     * @return 修复后的路径
     */
    public static String normalize(String path) {
        if (path == null) {
            return null;
        }

        // 兼容Spring风格的ClassPath路径，去除前缀，不区分大小写
        String pathToUse = StrUtil.removePrefixIgnoreCase(path, URLUtil.CLASSPATH_URL_PREFIX);
        // 去除file:前缀
        pathToUse = StrUtil.removePrefixIgnoreCase(pathToUse, URLUtil.FILE_URL_PREFIX);
        // 统一使用斜杠
        pathToUse = pathToUse.replaceAll("[/\\\\]{1,}", StrUtil.SLASH).trim();

        int prefixIndex = pathToUse.indexOf(StrUtil.COLON);
        String prefix = "";
        if (prefixIndex > -1) {
            // 可能Windows风格路径
            prefix = pathToUse.substring(0, prefixIndex + 1);
            if (StrUtil.startWith(prefix, StrUtil.C_SLASH)) {
                // 去除类似于/C:这类路径开头的斜杠
                prefix = prefix.substring(1);
            }
            if (false == prefix.contains(StrUtil.SLASH)) {
                pathToUse = pathToUse.substring(prefixIndex + 1);
            } else {
                // 如果前缀中包含/,说明非Windows风格path
                prefix = StrUtil.EMPTY;
            }
        }
        if (pathToUse.startsWith(StrUtil.SLASH)) {
            prefix += StrUtil.SLASH;
            pathToUse = pathToUse.substring(1);
        }

        List<String> pathList = StrUtil.splitToList(pathToUse, StrUtil.C_SLASH);
        List<String> pathElements = new LinkedList<String>();
        int tops = 0;

        String element;
        for (int i = pathList.size() - 1; i >= 0; i--) {
            element = pathList.get(i);
            if (StrUtil.DOT.equals(element)) {
                // 当前目录，丢弃
            } else if (StrUtil.DOUBLE_DOT.equals(element)) {
                tops++;
            } else {
                if (tops > 0) {
                    // 有上级目录标记时按照个数依次跳过
                    tops--;
                } else {
                    // Normal path element found.
                    pathElements.add(0, element);
                }
            }
        }

        return prefix + CollUtil.join(pathElements, StrUtil.SLASH);
    }

    /**
     * 可读的文件大小
     *
     * @param file 文件
     * @return 大小
     */
    public static String readableFileSize(File file) {
        return readableFileSize(file.length());
    }

    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 转换文件编码<br>
     * 此方法用于转换文件编码，读取的文件实际编码必须与指定的srcCharset编码一致，否则导致乱码
     *
     * @param file 文件
     * @param srcCharset 原文件的编码，必须与文件内容的编码保持一致
     * @param destCharset 转码后的编码
     * @return 被转换编码的文件
     * @since 3.1.0
     */
    public static File convertCharset(File file, Charset srcCharset, Charset destCharset) {
        final String str = readString(file, srcCharset);
        return writeString(str, file, destCharset.toString());
    }


    /**
     * 打印新行
     * @param writer Writer
     * @param lineSeparator 换行符枚举
     * @since 4.0.5
     */
    private static void printNewLine(PrintWriter writer, LineSeparator lineSeparator) {
        if(null == lineSeparator) {
            //默认换行符
            writer.println();
        }else {
            //自定义换行符
            writer.print(lineSeparator.getValue());
        }
    }


}
