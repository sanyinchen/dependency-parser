package com.sanyinchen.parser.lexer.exceptions;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * this exception is something is going wrong while processing an AST
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class ParseTreeProcessorException extends Exception {
    private static final long serialVersionUID = -3663250114314629370L;

    /**
     * constructor
     *
     * @param msg exception message
     */
    public ParseTreeProcessorException(String msg) {
        super(msg);
    }

    /**
     * constructor
     *
     * @param msg   exception message
     * @param cause the cause
     */
    public ParseTreeProcessorException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
