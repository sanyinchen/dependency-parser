package com.sanyinchen.parser.lexer.listener;

import com.sun.istack.internal.NotNull;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * ANTLR4 default parserListeer
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public abstract class DefaultListener implements ParseTreeListener, Serializable {

    private static final long serialVersionUID = 7449676975470436260L;
    public static final int HID_CHANNEL = 2;
    protected Parser parser;

    private final Map<Integer, String> ruleNameMap = new HashMap<>();

    /**
     * constructor
     */
    public DefaultListener() {
        parser = null;
    }

    /**
     * maps rule index to its actual name
     *
     * @param key rule index
     * @return the corresponding rule name
     */
    public String getRuleByKey(int key) {
        return ruleNameMap.get(key);
    }

    /**
     * set com.syc.lexer.parser
     *
     * @param p com.syc.lexer.parser
     */
    public void setParser(@NotNull Parser p) {
        parser = p;
        ruleNameMap.clear();
        parser.getRuleIndexMap().forEach((name, id) -> ruleNameMap.put(id, name));
    }

    /**
     * reset
     */
    public abstract void reset();


}
