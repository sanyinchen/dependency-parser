package com.sanyinchen.parser.lexer.grammar;


import com.sanyinchen.parser.lexer.exceptions.ParsingException;
import com.sanyinchen.parser.lexer.listener.DefaultTreeListener;
import com.sanyinchen.parser.lexer.listener.InmemantlrErrorListener;
import com.sanyinchen.parser.lexer.stream.DefaultStreamProvider;
import com.sanyinchen.parser.tree.ParseTree;
import com.sun.istack.internal.NotNull;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by sanyinchen on 19-3-27.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-27
 */

public abstract class DefaultFileParser implements FileParserInterface {
    @Override
    public ParseTree parse(@NotNull String content) {
        DefaultTreeListener listener = getParseTreeListener();
        try {
            parse(content, listener);
        } catch (ParsingException e) {
            e.printStackTrace();
            return null;
        }
        return listener.getParseTree();
    }

    protected abstract DefaultTreeListener getParseTreeListener();

    /**
     * 调用ANTLR4 解析java文件
     *
     * @param toParse
     * @param listener
     * @return
     * @throws ParsingException
     */
    private ParserRuleContext parse(String toParse, @NotNull DefaultTreeListener listener) throws ParsingException {
        InmemantlrErrorListener el = new InmemantlrErrorListener();


        CharStream input = new DefaultStreamProvider().getCharStream(toParse);

        Objects.requireNonNull(input, "char stream must not be null");

        Lexer lex = getFileLexer(input);
        lex.addErrorListener(el);
        Objects.requireNonNull(lex, "lex must not be null");

        CommonTokenStream tokens = new CommonTokenStream(lex);
        tokens.fill();

        CommonTokenStream cmtTokens = new CommonTokenStream(lex);
        cmtTokens.fill();

        Parser parser = getFileParser(tokens);
        Objects.requireNonNull(parser, "Parser must not be null");

        parser.removeErrorListeners();
        parser.addErrorListener(el);
        parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
        parser.setBuildParseTree(true);
        parser.setTokenStream(tokens);

        ParserRuleContext data = null;
        try {
            Class<?> pc = parser.getClass();
            Method m = pc.getDeclaredMethod(getRule(), (Class<?>[]) null);
            Objects.requireNonNull(m, "method should not be null");
            data = (ParserRuleContext) m.invoke(parser, (Object[]) null);
        } catch (NoSuchMethodException | SecurityException |
                IllegalAccessException | IllegalArgumentException |
                InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
        // todo
//        Set<String> msgs = el.getLog().entrySet()
//                .stream()
//                .filter(e -> e.getKey() == InmemantlrErrorListener.Type.SYNTAX_ERROR)
//                .map(Map.Entry::getValue)
//                .collect(Collectors.toSet());
//
//
//        if (!msgs.isEmpty()) {
//            throw new ParsingException(String.join("", msgs));
//        }
        listener.reset();
        listener.setParser(parser);
        new ParseTreeWalker().walk(listener, data);

        return data;
    }

    protected abstract Lexer getFileLexer(@NotNull CharStream intStream);

    protected abstract Parser getFileParser(@NotNull TokenStream tokenStream);

    @NotNull
    protected abstract String getRule();
}
