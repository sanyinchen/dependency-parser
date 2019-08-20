package com.sanyinchen.parser.util;


import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by sanyinchen on 19-3-27.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-27
 */

public class ParseTreeNodeFinder {

    private List<ParseTreeNode> parseTreeNodes = new ArrayList<>();

    /**
     * values are from build config
     */
    private boolean isSingleMode;
    private boolean reverse;
    private boolean first;

    private ParseTreeNodeFinder(@NotNull Builder builder) {
        isSingleMode = builder.isSingleMode;
        reverse = builder.reverse;
        first = builder.first;
    }

    private void collectNode(ParseTreeNode node) {
        if (parseTreeNodes.contains(node)) {
            return;
        }
        if (!isSingleMode) {
            parseTreeNodes.add(node);
            return;
        }
        if (isSingleMode && isSingleLine(node)) {
            parseTreeNodes.add(node);
            return;
        }

    }

    private boolean isSingleLine(@NotNull ParseTreeNode node) {
        return node.getSline() == node.getEline();
    }

    private void searchNeededNodes(@NotNull ParseTreeNode node,
                                   String rule, int sLine, @Nullable NodeFinerRule ruleFunc) {
        searchNeededNodes(null, node, rule, sLine, ruleFunc);
    }

    /**
     * 寻找符合规则匹配的节点
     *
     * @param node
     * @param rule
     * @param sLine
     */
    private void searchNeededNodes(@Nullable Predicate<ParseTreeNode> stopPredicate, @NotNull ParseTreeNode node,
                                   String rule, int sLine, @Nullable NodeFinerRule ruleFunc) {

        // 自定义终止规则
        if (stopPredicate != null && stopPredicate.test(node)) {
            return;
        }
        // first 选项加速某些节点查找
        if (first && parseTreeNodes.size() != 0) {
            return;
        }
        do {
            // 匹配RULE
            if (rule != null && !"".equals(rule)) {
                if (rule.equals(node.getRule())) {
                    collectNode(node);
                }
                break;
            }
            // 匹配源代码行数
            if (sLine != -1) {
                if (node.getSline() == sLine) {
                    collectNode(node);
                }
                break;
            }
            // 匹配任意规则
            if (ruleFunc != null) {
                if (ruleFunc.hit(node)) {
                    collectNode(node);
                }
                break;
            }


        } while (false);

        if (reverse) {
            if (!node.hasParent()) {
                return;
            }
            searchNeededNodes(stopPredicate, node.getParent(), rule, sLine, ruleFunc);
        } else {
            if (!node.hasChildren()) {
                return;
            }
            List<ParseTreeNode> children = node.getChildren();
            for (ParseTreeNode item : children) {
                searchNeededNodes(stopPredicate,item, rule, sLine, ruleFunc);
            }
        }
    }

    @NotNull
    public List<ParseTreeNode> getNeededNodes(ParseTreeNode node, String rule) {
        parseTreeNodes.clear();
        searchNeededNodes(node, rule, -1, null);
        return parseTreeNodes;
    }

    @NotNull
    public List<ParseTreeNode> getNeededNodes(ParseTreeNode node, int sourceLine) {
        parseTreeNodes.clear();
        searchNeededNodes(node, "", sourceLine, null);
        return parseTreeNodes;
    }

    @NotNull
    public List<ParseTreeNode> getNeededNodes(ParseTreeNode node, NodeFinerRule func) {
        parseTreeNodes.clear();
        searchNeededNodes(node, "", -1, func);
        return parseTreeNodes;
    }

    /**
     * 根据规则返回节点
     *
     * @param stopRule 终止规则
     * @param node     开始便利的根结点
     * @param func     自定义匹配规则
     * @return
     */
    @NotNull
    public List<ParseTreeNode> getNeededNodes(Predicate<ParseTreeNode> stopRule, ParseTreeNode node,
                                              NodeFinerRule func) {
        parseTreeNodes.clear();
        searchNeededNodes(stopRule, node, "", -1, func);
        return parseTreeNodes;
    }

    public interface NodeFinerRule {
        boolean hit(@NotNull ParseTreeNode node);
    }


    public static class Builder {
        /**
         * 是否只查找单行叶子节点
         */
        private boolean isSingleMode = false;
        /**
         * 是否反向查找
         */
        private boolean reverse = false;
        /**
         * 匹配到第一个节点即停止
         */
        private boolean first = false;

        public Builder setFirst(boolean first) {
            this.first = first;
            return this;
        }

        public Builder setSingleMode(boolean leafMode) {
            isSingleMode = leafMode;
            return this;
        }

        public Builder setReverse(boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public ParseTreeNodeFinder create() {
            return new ParseTreeNodeFinder(this);
        }

        public ParseTreeNodeFinder create(@Nullable Builder newConfig) {
            if (newConfig == null) {
                return new ParseTreeNodeFinder(this);
            }
            return new ParseTreeNodeFinder(newConfig);
        }

    }
}
