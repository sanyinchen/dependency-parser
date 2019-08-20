package com.sanyinchen.parser;

import com.sanyinchen.parser.async.basic.BasicTaskDispatchPool;
import com.sanyinchen.parser.async.basic.ThreadDisPatchManager;
import com.sanyinchen.parser.async.common.Pair;
import com.sanyinchen.parser.lexer.CodeLexerParser;
import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.util.FileFinderUtil;
import com.sanyinchen.parser.util.FileUtils;
import com.sun.istack.internal.NotNull;

import org.jd.core.v1.ClassFileToJavaSourceDecompiler;
import org.jd.core.v1.api.loader.Loader;
import org.jd.core.v1.api.loader.LoaderException;
import org.jd.core.v1.api.printer.Printer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public class CodeDependencyParser {
    private boolean useCache = false;
    private String cachePath = ".cache";

    public void parse(@NotNull String... jarPaths) {
        CodeDependencyParserAsync codeDependencyParserAsync = new CodeDependencyParserAsync();

        List<String> classesPath = new ArrayList<>();
        String jarFilesPath = cachePath + File.separator + "classes";
        File classesDir = new File(jarFilesPath);
        if (classesDir.exists()) {
            classesDir.delete();
        }
        try {
            String jarPath = jarPaths[0];
            //for (String jarPath : jarPaths) {
            File jar = new File(jarPath);
            if (!jar.exists() || !jar.isFile()) {
                throw new IllegalArgumentException("jar path is not valid");
            }
            FileUtils.jarDecompress(jarPath, classesDir.getPath());
            classesPath.addAll(FileFinderUtil.createNewFinder().classFileFind(jarFilesPath));

            //}
            System.out.println("scanned " + classesPath.size() + " .class file");
            String classFilePath = classesPath.get(0);
            codeDependencyParserAsync.addTask(classFilePath);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CodeDependencyParserAsync extends BasicTaskDispatchPool<String,
            CodeDependencyParser.Response> {
        @Override
        protected int getMaxThread() {
            return 1;
        }

        @Override
        protected int getMaxSingleThreadTask() {
            return 1;
        }

        @Override
        protected Response runTask(final String arg) {
            //if (useCache) {

            //}

            Loader loader = new Loader() {
                @Override
                public byte[] load(String internalName) throws LoaderException {
                    InputStream is = this.getClass().getResourceAsStream("/" + internalName + ".class");
                    if (is == null) {
                        return null;
                    } else {
                        try (InputStream in = is; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                            byte[] buffer = new byte[1024];
                            int read = in.read(buffer);

                            while (read > 0) {
                                out.write(buffer, 0, read);
                                read = in.read(buffer);
                            }

                            return out.toByteArray();
                        } catch (IOException e) {
                            throw new LoaderException(e);
                        }
                    }
                }

                @Override
                public boolean canLoad(String internalName) {
                    return this.getClass().getResource("/" + internalName + ".class") != null;
                }
            };
            ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
            try {
                System.out.println("1 ============>" + arg);
                decompiler.decompile(loader, new Printer.DefaultPrinter(), arg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String source = decompiler.toString();

            System.out.println(source);

//                String res = CodeLexerParser.getInstance().parseSource(FileUtils.loadFileContent(arg),
//                        CodeLexerParser.FileType.JAVA);
//                if (res == null || res.length() < 10) {
//                    interrupt();
//                }
            return null;
        }

        @Override
        protected ThreadDisPatchManager.ThreadTaskFinished<String, Response> getFinishedCallback() {
            return new ThreadDisPatchManager.ThreadTaskFinished<String, Response>() {
                @Override
                public void onFinished(List<Pair<String, Response>> finishedList,
                                       List<Pair<String, Response>> interruptedList) {

                }
            };
        }

        @Override
        protected ThreadDisPatchManager.JobTaskFinished<String, Response> getItemTaskCallback() {
            return new ThreadDisPatchManager.JobTaskFinished<String, Response>() {
                @Override
                public void onInterrupted(String inputArgs) {
                    System.out.println(inputArgs + " parse error !!");
                }

                @Override
                public void onFinished(Pair<String, Response> res) {

                }
            };
        }
    }

    public static class Response {
        private List<String> matchedPackages = new ArrayList<>();
        private List<String> misMatchedPackages = new ArrayList<>();
        private String packageName = "";

    }
}
