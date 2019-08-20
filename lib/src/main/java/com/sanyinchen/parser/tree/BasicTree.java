package com.sanyinchen.parser.tree;

import com.sun.istack.internal.NotNull;

import java.util.List;


/**
 * Created by sanyinchen on 19-3-27.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-27
 */

public abstract class BasicTree {
    /**
     * 默认根结点
     */
    public static final String DECLARATION_ROOT = "root";

    /**
     * 冲突节点字段声明
     */
    public static final String DECLARATION_CONFLICT = "cf";

    /**
     * 冲突节点ID
     */
    public static final String DECLARATION_CONFLICT_ID = "cf_id";

    /**
     * 节点状态
     */
    public static final String DECLARATION_STATUS_CODE = "status_code";


    /**
     * 当前节点类型
     */
    public static final String BUILD_NODE_TYPE = "nt";
    /**
     * 压缩后的代码范围 str=start+','+end such as 1,2
     */
    public static final String BUILD_CODE_RANGE = "ran";
    /**
     * 源码所在行数 str=startLine+','+endLine
     */
    public static final String BUILD_LINE_RANGE = "lran";
    /**
     * 当前压缩代码
     */
    public static final String BUILD_LABEL = "lbl";
    /**
     * AST树子节点
     */
    public static final String BUILD_NODE_CHILD = "cld";

    protected ParseTree parseTree;
    protected ParseTreeNode nodeptr;

    public BasicTree() {
        parseTree = new ParseTree("root", "root");
        nodeptr = parseTree.getRoot();

    }

    /**
     * 获取AST解析树
     *
     * @return
     */
    @NotNull
    public ParseTree getParseTree() {
        if (parseTree == null) {
            throw new UnsupportedOperationException("getParseTree can not get null parseTree ");
        }
        return parseTree;
    }


    /**
     * 扩展node节点
     *
     * @param newNode
     * @return
     */
    public BasicTree expandNode(@NotNull ParseTreeNode newNode) {
        newNode.setParent(nodeptr);
        addNode(newNode);
        return this;
    }

    /**
     * 添加一个node子节点
     *
     * @param newNode
     * @return
     */
    private BasicTree addNode(@NotNull ParseTreeNode newNode) {
        nodeptr.addChild(newNode);
        nodeptr = newNode;
        return this;
    }


    public BasicTree addNode(String nt, String label, int sidx, int eidx, int sline, int eline,
                             ParseTreeNodeStatus status) {
        ParseTreeNode newNode = parseTree.newNode(nodeptr, nt, label, sidx, eidx, sline, eline, status);
        addNode(newNode);
        return this;
    }

    /**
     * 在当前节点添加孩子节点
     *
     * @param childNode
     * @return
     */
    public BasicTree addChild(@NotNull ParseTreeNode childNode) {
        nodeptr.addChild(childNode);
        return this;
    }

    /**
     * 在当前节点添加孩子节点
     *
     * @param nt
     * @param label
     * @param sidx
     * @param eidx
     * @param sline
     * @param eline
     * @return
     */
    public BasicTree addChild(String nt, String label, int sidx, int eidx, int sline, int eline,
                              ParseTreeNodeStatus status) {
        ParseTreeNode newNode = parseTree.newNode(nodeptr, nt, label, sidx, eidx, sline, eline, status);
        addChild(newNode);
        return this;
    }


    /**
     * 在当前节点添加孩子节点
     *
     * @param childNode
     * @return
     */

    public BasicTree addChilds(@NotNull List<ParseTreeNode> childNode) {
        nodeptr.addChilds(childNode);
        return this;
    }


    public ParseTreeNode getRootNode() {
        return getParseTree().getRoot();
    }
}
