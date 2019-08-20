package com.sanyinchen.parser.lexer.exceptions;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * this exception is something is going wrong when injecting subtree into AST
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class InjectionException extends Exception {
    private static final long serialVersionUID = -3663250114312529370L;

    /**
     * constructor
     *
     * @param msg exception message
     */
    public InjectionException(String msg) {
        super(msg);
    }

    /**
     * constructor
     *
     * @param msg   exception message
     * @param cause the cause
     */
    public InjectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
