package com.sanyinchen.parser.property.java;

import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sun.istack.internal.NotNull;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public enum JavaNodeUtil {
    INS;

    public String getEscapePackageName(@NotNull ParseTreeNode node) {
        return getEscapePackageName(node.getLabel());
    }

    private String getEscapePackageName(@NotNull String nodeLbl) {
        return nodeLbl.replace("\\\\", "");
    }

    public String getEscapeImportPackageName(@NotNull ParseTreeNode node) {
        String lbl = node.getLabel();
        return getEscapePackageName(lbl.replaceFirst("import", "import "));
    }
}
