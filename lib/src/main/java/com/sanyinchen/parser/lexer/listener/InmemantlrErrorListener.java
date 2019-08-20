package com.sanyinchen.parser.lexer.listener;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * This class is used in order to report parsing errors
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class InmemantlrErrorListener extends BaseErrorListener {


    public enum Type {
        SYNTAX_ERROR,
        AMBIGUITY,
        FULL_CTX,
        CTX_SSTV
    }

    Map<Type, String> log = new HashMap<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        log.put(Type.SYNTAX_ERROR, "(line " + line + ",char " +
                charPositionInLine + "): " + msg);
    }

    @Override
    public void reportAmbiguity(Parser recognizer,
                                DFA dfa,
                                int startIndex,
                                int stopIndex,
                                boolean exact,
                                BitSet ambigAlts,
                                ATNConfigSet configs) {
        log.put(Type.AMBIGUITY, "Ambiguity " + startIndex + " " + stopIndex);
    }

    @Override
    public void reportAttemptingFullContext(Parser recognizer,
                                            DFA dfa,
                                            int startIndex,
                                            int stopIndex,
                                            BitSet conflictingAlts,
                                            ATNConfigSet configs) {
        log.put(Type.FULL_CTX, "Attempting full Context " + startIndex + " " +
                stopIndex);
    }

    @Override
    public void reportContextSensitivity(Parser recognizer,
                                         DFA dfa,
                                         int startIndex,
                                         int stopIndex,
                                         int prediction,
                                         ATNConfigSet configs) {
        log.put(Type.CTX_SSTV, "Context Sensitivity " + startIndex + " " +
                stopIndex);
    }


    public Map<Type, String> getLog() {
        return log;
    }

}
