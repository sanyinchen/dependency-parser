package com.sanyinchen.parser.property.java;


import com.sanyinchen.parser.async.common.Pair;
import com.sanyinchen.parser.tree.DefaultBasicTree;
import com.sanyinchen.parser.tree.ParseTree;
import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sanyinchen.parser.util.CollectionsUtils;
import com.sanyinchen.parser.util.ParseTreeNodeFinder;
import com.sun.istack.internal.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * JAVA 语法树
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class JavaTree extends DefaultBasicTree {

    /**
     * package
     */
    public static final String DECLARATION_PACKAGE = "packageDeclaration";
    public static final String DECLARATION_PACKAGE_IMPORT = "importDeclaration";


    public JavaTree() {
        super();
    }

    public JavaTree(@NotNull ParseTree parseTree) {
        super(parseTree);
    }

    public JavaTree(String originStr) {
        super(originStr);
    }


    /**
     * deepCopy
     *
     * @return
     */
    public JavaTree deepCopy() {
        return new JavaTree(this.getOriginStr());
    }

    /**
     * 判断是否在packageName声明节点
     *
     * @param node
     * @return
     */
    private boolean isInPackageNode(@NotNull ParseTreeNode node) {
        List<ParseTreeNode> nodes = new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create()
                .getNeededNodes(node, DECLARATION_PACKAGE);
        return nodes.size() != 0;

    }

    /**
     * 获取 package node节点
     *
     * @param node
     * @return
     */
    private ParseTreeNode getPackageNode(@NotNull ParseTreeNode node) {
        if (!isInPackageNode(node)) {
            throw new RuntimeException("not in package node");
        }
        List<ParseTreeNode> destNode =
                new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create().getNeededNodes(node,
                        DECLARATION_PACKAGE);
        if (!CollectionsUtils.isSingleItem(destNode)) {
            throw new RuntimeException(" can not find package node");
        }
        return CollectionsUtils.getFirstItem(destNode);
    }

    /**
     * 获取import package node节点
     *
     * @param node
     * @return
     */
    private ParseTreeNode getPackageImportNode(@NotNull ParseTreeNode node) {
        if (!isInPackageImportNode(node)) {
            throw new RuntimeException("not in package import node");
        }
        List<ParseTreeNode> destNode =
                new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create().getNeededNodes(node,
                        DECLARATION_PACKAGE_IMPORT);
        if (!CollectionsUtils.isSingleItem(destNode)) {
            throw new RuntimeException(" can not find package import node");
        }
        return CollectionsUtils.getFirstItem(destNode);
    }

    /**
     * 判断是否在import package 节点
     *
     * @param node
     * @return
     */
    private boolean isInPackageImportNode(@NotNull ParseTreeNode node) {
        List<ParseTreeNode> nodes = new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create()
                .getNeededNodes(node, DECLARATION_PACKAGE_IMPORT);
        return nodes.size() != 0;
    }

    public Pair<ParseTreeNode, Set<ParseTreeNode>> getPackageNode() {
        List<ParseTreeNode> parseTreeNodes = getParseTree().getNodes();
        ParseTreeNode packageNode = null;
        Set<ParseTreeNode> importPackageNodes = new HashSet<>();
        for (ParseTreeNode node : parseTreeNodes) {
            if (packageNode == null && isInPackageNode(node)) {
                packageNode = getPackageNode(node);
                continue;
            }
            if (isInPackageImportNode(node)) {
                importPackageNodes.add(getPackageImportNode(node));
                continue;
            }
        }
        return new Pair<>(packageNode, importPackageNodes);

    }

}
