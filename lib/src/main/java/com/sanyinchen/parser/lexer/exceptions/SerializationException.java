package com.sanyinchen.parser.lexer.exceptions;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * this exception is thrown when serialization fails
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class SerializationException extends Exception {

    private static final long serialVersionUID = -4037860200439144592L;

    /**
     * constructor
     *
     * @param msg exception message
     */
    public SerializationException(String msg) {
        super(msg);
    }

    /**
     * constructor
     *
     * @param msg   exception message
     * @param cause the cause
     */
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
