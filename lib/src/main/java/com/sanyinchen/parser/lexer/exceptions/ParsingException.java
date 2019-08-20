package com.sanyinchen.parser.lexer.exceptions;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * this exception is thrown when a parsing error occurrs
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class ParsingException extends Exception {

    private static final long serialVersionUID = -8475142705514927769L;

    /**
     * constructor
     *
     * @param msg exception message
     */
    public ParsingException(String msg) {
        super(msg);
    }

    /**
     * constructor
     *
     * @param msg   exception message
     * @param cause the cause
     */
    public ParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}