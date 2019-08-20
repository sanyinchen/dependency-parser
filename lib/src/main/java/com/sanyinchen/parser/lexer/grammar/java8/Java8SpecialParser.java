package com.sanyinchen.parser.lexer.grammar.java8;

import com.sanyinchen.parser.lexer.grammar.DefaultFileParser;
import com.sanyinchen.parser.lexer.listener.DefaultTreeListener;
import com.sun.istack.internal.NotNull;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.TokenStream;



/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * java8 语法解析器
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class Java8SpecialParser extends DefaultFileParser {


    @Override
    protected DefaultTreeListener getParseTreeListener() {

        return new DefaultTreeListener();
    }

    @Override
    protected Lexer getFileLexer(@NotNull CharStream intStream) {
        return new Java8Lexer(intStream);
    }

    @Override
    protected Parser getFileParser(@NotNull TokenStream tokenStream) {
        return new Java8Parser(tokenStream);
    }

    @NotNull
    @Override
    protected String getRule() {
        return "compilationUnit";
    }
}
