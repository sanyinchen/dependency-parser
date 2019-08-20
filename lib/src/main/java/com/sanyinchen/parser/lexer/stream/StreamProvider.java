
package com.sanyinchen.parser.lexer.stream;

import org.antlr.v4.runtime.CharStream;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * stream provider interface used for lexing
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public interface StreamProvider {
    CharStream getCharStream(String s);
}
