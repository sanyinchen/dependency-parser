
package com.sanyinchen.parser.util;

import com.sun.istack.internal.NotNull;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.IOUtils;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * 文件操作帮助类
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public final class FileUtils {

    private FileUtils() {
    }

    /**
     * load file content
     *
     * @param path path of the file to load
     * @return file content as string
     */
    public static String loadFileContent(File path) {
        return loadFileContent(path.getAbsolutePath());
    }

    /**
     * load file content
     *
     * @param path path of the file to load
     * @return file content as string
     */
    public static String loadFileContent(String path) {
        byte[] bytes;
        try (RandomAccessFile f = new RandomAccessFile(path, "r")) {
            bytes = new byte[(int) f.length()];
            f.read(bytes);
        } catch (IOException e) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }


    /**
     * load input stream
     *
     * @param is input stream
     * @return stream content as string
     */
    public static String getStringFromStream(InputStream is) {
        try {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * writes string to file
     *
     * @param string string to write
     * @param file   file to which the string shall be written
     * @throws FileExistsException is thrown if file already exists
     */
    public static void writeStringToFile(String string, String file) throws
            FileExistsException, FileNotFoundException {

        if (Files.exists(Paths.get(file))) {
            throw new FileExistsException("File " + file + " already exists");
        }


        File f = new File(file);

        f.getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(f)) {
            out.write(string);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.toString());
        }
    }

    /**
     * get Redirect path
     *
     * @param filePath
     * @param fileProjectPath
     * @param outPutProjectPath
     * @return
     */
    public static String getRedirectFilePath(@NotNull String filePath, @NotNull String fileProjectPath,
                                             @NotNull String outPutProjectPath) {

        String relative = new File(fileProjectPath).toURI().relativize(new File(filePath).toURI()).getPath();

        return outPutProjectPath + File.separator + relative;


    }

    /**
     * check file is existed
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsExisted(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File temp = new File(filePath);
        return temp.exists() && temp.isFile();
    }

    public static void mkdirs(@NotNull String path, boolean isFile) {
        File file = new File(path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }
        if (isFile) {
            file = file.getParentFile();
        }

        if (file.isDirectory() && file.exists()) {
            return;
        } else {
            file.mkdirs();
        }
    }


    public static boolean isExisted(@NotNull String path) {
        return new File(path).exists();
    }


    public static void jarDecompress(@NotNull String jarPath, @NotNull String outputPath) {
        File jarDir = new File(jarPath);
        if (!jarDir.exists() || !jarDir.isFile()) {
            return;
        }
        if (!outputPath.endsWith(File.separator)) {
            outputPath += File.separator;
        }
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        JarFile jf = null;
        try {
            jf = new JarFile(jarPath);
            for (Enumeration<JarEntry> e = jf.entries(); e.hasMoreElements(); ) {
                JarEntry je = (JarEntry) e.nextElement();
                String outFileName = outputPath + je.getName();
                File f = new File(outFileName);
                if (je.isDirectory()) {
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                } else {
                    File pf = f.getParentFile();
                    if (!pf.exists()) {
                        pf.mkdirs();
                    }
                    InputStream in = jf.getInputStream(je);
                    OutputStream out = new BufferedOutputStream(
                            new FileOutputStream(f));
                    byte[] buffer = new byte[2048];
                    int nBytes = 0;
                    while ((nBytes = in.read(buffer)) > 0) {
                        out.write(buffer, 0, nBytes);
                    }
                    out.flush();
                    out.close();
                    in.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void find(String pathName, int depth) throws IOException {
        int filecount = 0;
        //获取pathName的File对象
        File dirFile = new File(pathName);
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists()) {
            System.out.println("do not exit");
            return;
        }
        //判断如果不是一个目录，就判断是不是一个文件，时文件则输出文件路径
        if (!dirFile.isDirectory()) {
            if (dirFile.isFile()) {
                System.out.println(dirFile.getCanonicalFile());
            }
            return;
        }

        for (int j = 0; j < depth; j++) {
            System.out.print("  ");
        }
        System.out.print("|--");
        System.out.println(dirFile.getName());
        //获取此目录下的所有文件名与目录名
        String[] fileList = dirFile.list();
        int currentDepth = depth + 1;
        for (int i = 0; i < fileList.length; i++) {
            //遍历文件目录
            String string = fileList[i];
            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(), string);
            String name = file.getName();
            //如果是一个目录，搜索深度depth++，输出目录名后，进行递归
            if (file.isDirectory()) {
                //递归
                find(file.getCanonicalPath(), currentDepth);
            } else {
                //如果是文件，则直接输出文件名
                for (int j = 0; j < currentDepth; j++) {
                    System.out.print("   ");
                }
                System.out.print("|--");
                System.out.println(name);

            }
        }
    }


}
