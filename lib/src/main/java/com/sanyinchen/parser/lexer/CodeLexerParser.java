package com.sanyinchen.parser.lexer;

import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.lexer.grammar.java8.JavaCommonLexer;
import com.sun.istack.internal.NotNull;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * 文件解析器包装类
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class CodeLexerParser {


    public enum FileType {
        UNKNOWN,
        JAVA
    }

    private volatile static CodeLexerParser INS;

    private CodeLexerParser() {

    }

    public static CodeLexerParser getInstance() {
        if (INS == null) {
            synchronized (CodeLexerParser.class) {
                if (INS == null) {
                    INS = new CodeLexerParser();
                }
            }
        }
        return INS;
    }

    /**
     * parse single source code
     *
     * @param content source code
     * @param type    type of source file
     * @return Json String
     */
    public String parseSource(@NotNull String content, FileType type)
            throws ParseTreeProcessorException {
        switch (type) {
            case JAVA:
                return new JavaCommonLexer(content).result();
            default:
                throw new UnsupportedOperationException("not support this file type");
        }
    }


}
