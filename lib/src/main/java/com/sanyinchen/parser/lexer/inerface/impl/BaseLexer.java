package com.sanyinchen.parser.lexer.inerface.impl;


import com.sanyinchen.parser.lexer.CodeLexerParser;
import com.sanyinchen.parser.lexer.inerface.ParseSourceCode;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * basic parser class
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public abstract class BaseLexer<T> implements ParseSourceCode<T> {
    private CodeLexerParser.FileType mFileType;
    private String sourceCode;

    public BaseLexer(CodeLexerParser.FileType fileType, String sourceCode) {
        mFileType = fileType;
        this.sourceCode = sourceCode;

    }

    public CodeLexerParser.FileType getFileType() {
        return mFileType;
    }

    public String getSourceCode() {
        return sourceCode;
    }

}
