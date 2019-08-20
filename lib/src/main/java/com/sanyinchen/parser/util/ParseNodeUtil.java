package com.sanyinchen.parser.util;

import com.sun.istack.internal.NotNull;

import org.antlr.v4.runtime.Token;

/**
 * Created by sanyinchen on 19-4-9.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-4-9
 */

public class ParseNodeUtil {

    /**
     * 获取注释节点尾行号
     *
     * @param cmtToken
     * @return
     */
    public static int getCmtLine(@NotNull Token cmtToken) {
        int sLine = cmtToken.getLine();
        for (int i = 0; i < cmtToken.getText().length(); i++) {
            if ('\n' == cmtToken.getText().charAt(i)) {
                sLine++;
            }
        }
        return sLine;
    }



}
