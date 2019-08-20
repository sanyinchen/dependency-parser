package com.sanyinchen.parser.util;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public class FileFinderUtil {

    private List<String> files = new ArrayList<>();

    public static FileFinderUtil createNewFinder() {
        return new FileFinderUtil();
    }

    public List<String> classFileFind(@NotNull String path) throws IOException {
        fileFind(path, ".class");
        return files;
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
        for (int i = 0; i < fileList.length; i++) {
            String string = fileList[i];
            File file = new File(dirFile.getPath(), string);
            if (file.isDirectory()) {
                fileFind(file.getCanonicalPath(), filiter);
            } else {
                if (filiter == null) {
                    files.add(file.getCanonicalPath());
                } else {
                    if (file.getCanonicalPath().endsWith(filiter)) {
                        files.add(file.getCanonicalPath());
                    }
                }
            }
        }
    }

}
