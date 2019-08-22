package com.sanyinchen.parser.command;


import com.sanyinchen.parser.CodeDependencyParser;
import com.sanyinchen.parser.util.FileUtils;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;


public class Command {
    public static void main(String[] args) throws IOException {
        if (args == null || args.length != 1) {
            System.out.println("args invalid ");
            return;
        }

        String jarDir = args[0];
        File jarFileDir = new File(jarDir);
        String jarParentDir = jarFileDir.getCanonicalPath();
        if (!FileUtils.isExisted(jarDir)) {
            System.out.println(jarDir + " is not existed");
        }
        String[] jarNames = jarFileDir.list();
        String[] jarFiles = new String[jarNames.length];
        for (int i = 0; i < jarNames.length; i++) {
            jarFiles[i] = jarParentDir + File.separator + jarNames[i];
        }
        new CodeDependencyParser().parse(jarFiles);
    }


}
