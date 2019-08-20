package com.sanyinchen.demo;

import com.sanyinchen.parser.CodeDependencyParser;
import com.sanyinchen.parser.lexer.CodeLexerParser;
import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.util.FileUtils;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public class FooAstTest {
    public static void main(String[] args) {
        try {
            CodeLexerParser.getInstance().parseSource(FileUtils.loadFileContent("./.cache/test.txt"),
                    CodeLexerParser.FileType.JAVA);
        } catch (ParseTreeProcessorException e) {
            e.printStackTrace();
        }
    }
}
