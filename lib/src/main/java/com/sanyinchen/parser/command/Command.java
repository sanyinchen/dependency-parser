package com.sanyinchen.parser.command;


import java.awt.List;
import java.io.File;

public class Command {
    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.out.println("args invalid ");
            return;
        }

        String jarDir = args[0];
        File jarFileDir = new File(jarDir);
        String[] jarNames = jarFileDir.list();
        for (String str : jarNames) {
            System.out.println("str:" + str);
        }

    }


}
