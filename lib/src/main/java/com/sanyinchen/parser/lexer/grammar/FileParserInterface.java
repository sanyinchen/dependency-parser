package com.sanyinchen.parser.lexer.grammar;

import com.sanyinchen.parser.tree.ParseTree;
import com.sun.istack.internal.NotNull;

/**
 * Created by sanyinchen on 19-3-27.
 * lexter 基础类
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-27
 */

public interface FileParserInterface {
    ParseTree parse(@NotNull String content);

}
