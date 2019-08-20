package com.sanyinchen.parser.lexer.grammar.java8;

import com.sanyinchen.parser.lexer.CodeLexerParser;
import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.lexer.inerface.impl.BaseLexer;
import com.sanyinchen.parser.tree.ParseTree;
import com.sun.istack.internal.NotNull;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * Java 文件解析
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class JavaCommonLexer extends BaseLexer<String> {
    private static Java8SpecialParser java8SpecialParser;

    static {
        java8SpecialParser = new Java8SpecialParser();

    }

    public JavaCommonLexer(@NotNull String sourceCode) {
        super(CodeLexerParser.FileType.JAVA, sourceCode);
    }

    @Override
    public String result() throws ParseTreeProcessorException {

        ParseTree pt = java8SpecialParser.parse(getSourceCode());
        return pt != null ? pt.toJson() : "{}";
    }
}
