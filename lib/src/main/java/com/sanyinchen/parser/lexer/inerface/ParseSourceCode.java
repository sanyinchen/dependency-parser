package com.sanyinchen.parser.lexer.inerface;


import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * file parser
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public interface ParseSourceCode<T> {

    T result() throws ParseTreeProcessorException;

}
