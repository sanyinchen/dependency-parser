
package com.sanyinchen.parser.util;


import com.sanyinchen.parser.tree.ParseTree;
import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sun.istack.internal.Nullable;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * AST 树操作接口
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public interface InjectionPointDetector {

    enum Position {
        BEFORE,
        AFTER,
        REPLACED
    }

    /**
     * 检索规则
     *
     * @param t
     * @return
     */
    @Nullable
    ParseTreeNode detect(ParseTree t);

    /**
     * 指明插入位置
     *
     * @return
     */
    Position getPosition();
}
