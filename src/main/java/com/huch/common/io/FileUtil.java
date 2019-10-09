package com.huch.common.io;

import com.huch.common.util.ArrayUtil;
import com.huch.common.util.StrUtil;
import com.huch.common.util.URLUtil;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
     * 是否为Windows环境
     *
     * @return 是否为Windows环境
     * @since 3.0.9
     */
    public static boolean isWindows() {
        return WINDOWS_SEPARATOR == File.separatorChar;
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
        List<File> fileList = new ArrayList<>();
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


}
