package com.sanyinchen.parser.lexer.stream;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * default stream provider
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class DefaultStreamProvider implements StreamProvider {
    @Override
    public CharStream getCharStream(String s) {
        return CharStreams.fromString(s);
    }
}
