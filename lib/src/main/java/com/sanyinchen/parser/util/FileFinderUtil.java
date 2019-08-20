package com.sanyinchen.parser.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.metadata.IIOInvalidTreeException;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public class FileFinderUtil {

    private List<String> files = new ArrayList<>();
    private String dirPath;

    public static FileFinderUtil createNewFinder(String dirPath) {
        return new FileFinderUtil(dirPath);
    }

    public FileFinderUtil(String dirPath) {
        this.dirPath = dirPath;
    }

    public List<String> classFileFind(@NotNull String path) throws IOException {
        fileFind(path, ".class");
        return files;
    }

    private String getRelativePath(String filePath) throws IOException {
        String absFilePath = new File(filePath).getCanonicalPath();
        String[] filePaths = absFilePath.split(dirPath);
        if (filePaths.length != 2) {
            throw new RuntimeException("filePath is invalid:" + filePath);
        }
        String relativePath = filePaths[1];
        if (relativePath.startsWith(File.separator)) {
            return relativePath.replaceFirst(File.separator, "");
        }
        return relativePath;
    }

    private void fileFind(@NotNull String path, @Nullable String filiter) throws IOException {

        File dirFile = new File(path);

        if (!dirFile.exists()) {
            return;
        }
        if (!dirFile.isDirectory()) {
            return;
        }

        String[] fileList = dirFile.list();
        if (fileList == null) {
            return;
        }
        for (int i = 0; i < fileList.length; i++) {
            String string = fileList[i];
            File file = new File(dirFile.getPath(), string);
            if (file.isDirectory()) {
                fileFind(file.getCanonicalPath(), filiter);
            } else {
                if (filiter == null) {
                    files.add(getRelativePath(file.getCanonicalPath()));
                } else {
                    if (file.getCanonicalPath().endsWith(filiter)) {
                        files.add(getRelativePath(file.getCanonicalPath()));
                    }
                }
            }
        }
    }

}
