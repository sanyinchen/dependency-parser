package com.sanyinchen.parser;

import com.sanyinchen.parser.async.basic.BasicTaskDispatchPool;
import com.sanyinchen.parser.async.basic.ThreadDisPatchManager;
import com.sanyinchen.parser.async.common.Pair;
import com.sanyinchen.parser.lexer.CodeLexerParser;
import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.property.java.JavaNodeUtil;
import com.sanyinchen.parser.property.java.JavaTree;
import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sanyinchen.parser.util.FileFinderUtil;
import com.sanyinchen.parser.util.FileUtils;
import com.sun.istack.internal.NotNull;

import org.apache.commons.io.FileExistsException;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    private String astDir = cachePath + File.separator + "asts";
    private String jarFilesPath = cachePath + File.separator + "classes";

    public void parse(@NotNull String... jarPaths) {
        CodeDependencyParserAsync codeDependencyParserAsync = new CodeDependencyParserAsync();

        FileUtils.delExistedFile(cachePath);
        List<String> classesPath = new ArrayList<>();
        File classesDir = new File(jarFilesPath);
        try {

            for (String jarPath : jarPaths) {
                File jar = new File(jarPath);
                if (!jar.exists() || !jar.isFile()) {
                    throw new IllegalArgumentException("jar path is not valid");
                }
                FileUtils.jarDecompress(jarPath, classesDir.getPath());
                classesPath.addAll(FileFinderUtil.createNewFinder(new File(jarFilesPath).getCanonicalPath())
                        .classFileFind(jarFilesPath));

            }
            System.out.println("scanned " + classesPath.size() + " .class file");
            for (String classFilePath : classesPath) {
                codeDependencyParserAsync.addTask(new Request(new File(jarFilesPath).getCanonicalPath(),
                        classFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CodeDependencyParserAsync extends BasicTaskDispatchPool<Request,
            CodeDependencyParser.Response> {

        private boolean useCache = false;


        private String getAbsClassPath(String projectPath, String relativePath) {
            String absFile = projectPath;
            if (!absFile.endsWith(File.separator)) {
                absFile += File.separator;
            }
            return absFile + relativePath;
        }

        @Override
        protected Response runTask(final Request arg) {
            String parentDir = arg.getParentDir();
            String classFile = arg.getClassFilePath();

            //if (useCache) {

            //}

            Loader loader = new Loader() {
                @Override
                public byte[] load(String internalName) throws LoaderException {
                    if (internalName == null || !internalName.equals(classFile)) {
                        return null;
                    }

                    InputStream is = null;
                    try {
                        is = new FileInputStream(new File(getAbsClassPath(parentDir, internalName)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
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
                    return true;
                }
            };
            ClassFileToJavaSourceDecompiler decompiler = new ClassFileToJavaSourceDecompiler();
            try {
                decompiler.decompile(loader, new Printer.DefaultPrinter(), classFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String source = decompiler.getPrinterStr();

            String res = null;
            try {
                res = CodeLexerParser.getInstance().parseSource(source,
                        CodeLexerParser.FileType.JAVA);
            } catch (ParseTreeProcessorException e) {

                e.printStackTrace();
            }
            if (res == null || res.length() < 10) {
                Thread.currentThread().interrupt();
            }
            String astFile = "";
            try {
                astFile = getAbsClassPath(astDir, classFile).replace(".class", ".json");
                // todo cache
                FileUtils.delExistedFile(astFile);
                FileUtils.writeStringToFile(res, astFile);
            } catch (FileExistsException | FileNotFoundException e) {
                // e.printStackTrace();
            }
            JavaTree javaTree = new JavaTree(res);
            Pair<ParseTreeNode, Set<ParseTreeNode>> packages = javaTree.getPackageNode();
            ParseTreeNode packageNode = packages.first;
            Set<ParseTreeNode> importPackageNodes = packages.second;
            Set<String> importPackages = new HashSet<>();
            for (ParseTreeNode parseTreeNode : importPackageNodes) {
                importPackages.add(JavaNodeUtil.INS.getEscapeImportPackageName(parseTreeNode));
            }
            return new Response(importPackages, JavaNodeUtil.INS.getEscapePackageName(packageNode));
        }

        private boolean contains(@NotNull Object[] packages, @NotNull String packageName) {
            for (Object str : packages) {
                if (!str.equals("") && packageName.startsWith(((String) str)
                        .replaceFirst("package", "")
                        .replace(";", ""))) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected ThreadDisPatchManager.ThreadTaskFinished<Request, Response> getFinishedCallback() {
            return new ThreadDisPatchManager.ThreadTaskFinished<Request, Response>() {
                @Override
                public void onFinished(List<Pair<Request, Response>> finishedList,
                                       List<Pair<Request, Response>> interruptedList) {
                    if (interruptedList.size() != 0) {
                        System.out.println("parser error in ");
                        interruptedList.forEach(item -> {
                            System.out.println(item.first.classFilePath);
                        });
                    } else {
                        System.out.println("parser finished");
                        Set<String> needChecked = new HashSet<>();
                        Set<String> includePackages = new HashSet<>();
                        for (Pair<Request, Response> pair : finishedList) {
                            Request request = pair.first;
                            Response response = pair.second;
                            if (response == null) {
                                System.out.println("parser error at : " + request.classFilePath);
                                break;
                            }
                            includePackages.add(response.packageName);
                        }
                        for (Pair<Request, Response> pair : finishedList) {
                            Response response = pair.second;
                            for (String str : response.importPackages) {
                                if (str.replaceFirst("import ", "").startsWith("java.")) {
                                    continue;
                                }
                                if (contains(includePackages.toArray(), str.replaceFirst("import ", ""))) {
                                    continue;
                                }
                                needChecked.add(str);
                            }
                        }


                        TreeSet outTreeSet = new TreeSet(needChecked);
                        System.out.println("you need to check : " + needChecked.size() + " group");
                        outTreeSet.forEach(System.out::println);
                    }
                }
            };
        }

        @Override
        protected ThreadDisPatchManager.JobTaskFinished<Request, Response> getItemTaskCallback() {
            return new ThreadDisPatchManager.JobTaskFinished<Request, Response>() {
                @Override
                public void onInterrupted(Request inputArgs) {
                    System.out.println(inputArgs + " parse error !!");
                }

                @Override
                public void onFinished(Pair<Request, Response> res) {
                    System.out.println(res.first.classFilePath + " finished in " + Thread.currentThread());
                }
            };
        }
    }

    public static class Request {
        private String parentDir = "";
        private String classFilePath = "";

        public Request(@NotNull String parentDir, @NotNull String classFilePath) {
            this.parentDir = parentDir;
            this.classFilePath = classFilePath;
        }

        public String getParentDir() {
            return parentDir;
        }

        public String getClassFilePath() {
            return classFilePath;
        }
    }

    public static class Response {
        private Set<String> importPackages = new HashSet<>();
        private String packageName = "";

        public Response(@NotNull Set<String> importPackages, @NotNull String packageName) {
            this.importPackages = importPackages;
            this.packageName = packageName;
        }
    }
}
