package com.sanyinchen.parser.property.java;


import com.sanyinchen.parser.async.common.Pair;
import com.sanyinchen.parser.tree.DefaultBasicTree;
import com.sanyinchen.parser.tree.ParseTree;
import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sanyinchen.parser.util.CollectionsUtils;
import com.sanyinchen.parser.util.ParseTreeNodeFinder;
import com.sun.istack.internal.NotNull;

import org.stringtemplate.v4.ST;

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

    /**
     * class
     *
     */
    public static final String DECLARATION_DEFINE_TYPE_NAME = "typeNameDeclaration";
    public static final String DECLARATION_CLASS = "classDeclaration";


    /**
     * 枚举相关
     */
    public static final String DECLARATION_ENUM_BODY = "enumBody";
    public static final String DECLARATION_DEFINE_ENUM = "enumDeclaration";
    public static final String DECLARATION_DEFINE_ENUM_CONSTANT = "enumConstant";

    /**
     * interface 相关
     */
    public static final String DECLARATION_DEFINE_INTERFACE = "interfaceDeclaration";
    public static final String DECLARATION_INTERFACE_MODIFIER = "interfaceModifier";
    public static final String DECLARATION_DEFINE_NORMAL_INTERFACE = "normalInterfaceDeclaration";
    public static final String DECLARATION_INTERFACE_BODY = "interfaceBody";

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

    public Pair<String, Set<ParseTreeNode>> getPackageNode() {
        List<ParseTreeNode> parseTreeNodes = getParseTree().getNodes();
        ParseTreeNode packageNode = null;
        Set<ParseTreeNode> importPackageNodes = new HashSet<>();

        List<ParseTreeNode> innerNodes=
                new ParseTreeNodeFinder.Builder().setFirst(true).create().getNeededNodes(getRootNode(),
                node->isInClassNode(node)||isInEnumNode(node)||isInInterfaceNode(node));
        if (innerNodes.size()==0){
            throw new RuntimeException("inner nodes's size is 0");
        }
        String nameDefine=getClassOrInterfaceName(innerNodes.get(0));

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
        String className=new StringBuilder(JavaNodeUtil.INS.getEscapePackageName(packageNode).replace(";",""))
                .append(".").append(nameDefine).toString();

        return new Pair<>(className, importPackageNodes);

    }

    /**
     * 判断是否是enum 节点
     *
     * @param node
     * @return
     */
    public static boolean isInEnumNode(@NotNull ParseTreeNode node) {
        List<ParseTreeNode> nodes = new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create()
                .getNeededNodes(node, DECLARATION_DEFINE_ENUM);
        return nodes.size() != 0;
    }

    /**
     * 获取enum name
     *
     * @param node
     * @return
     */
    public static String getEnumName(@NotNull ParseTreeNode node) {
        if (!isInEnumNode(node)) {
            throw new RuntimeException("not in enum node ");
        }

        List<ParseTreeNode> names =
                new ParseTreeNodeFinder.Builder().setFirst(true).create()
                        .getNeededNodes(getEnumNode(node), DECLARATION_DEFINE_TYPE_NAME);
        if (!CollectionsUtils.isSingleItem(names)) {
            throw new RuntimeException("can not get enum header node");
        }
        return CollectionsUtils.getFirstItem(names).getLabel();
    }

    /**
     * 获取完整enum节点
     *
     * @param node
     * @return
     */
    public static ParseTreeNode getEnumNode(@NotNull ParseTreeNode node) {
        if (!isInEnumNode(node)) {
            throw new RuntimeException(" not in enum node");
        }
        List<ParseTreeNode> list =
                new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create().getNeededNodes(node,
                        DECLARATION_DEFINE_ENUM);
        if (!CollectionsUtils.isSingleItem(list)) {
            throw new RuntimeException("can not get get enum Node ,because of the size of list is :" + list.size());
        }

        return CollectionsUtils.getFirstItem(list);
    }

    /**
     * 获取interface的名称
     *
     * @param node
     * @return
     */
    public static String getInterfaceName(@NotNull ParseTreeNode node) {
        if (!isInInterfaceNode(node)) {
            throw new RuntimeException("not in interface node ");
        }

        List<ParseTreeNode> names =
                new ParseTreeNodeFinder.Builder().setFirst(true).create()
                        .getNeededNodes(getInterfaceNode(node), DECLARATION_DEFINE_TYPE_NAME);
        if (!CollectionsUtils.isSingleItem(names)) {
            throw new RuntimeException("can not get interface header node");
        }
        return CollectionsUtils.getFirstItem(names).getLabel();

    }

    /**
     * 获取类或者接口类的名称
     *
     * @param node
     * @return
     */
    public static String getClassOrInterfaceName(@NotNull ParseTreeNode node) {
        ParseTreeNode classNode;
        if (isInClassNode(node)) {
            classNode = getClassNode(node);
        } else if (isInInterfaceNode(node)) {
            classNode = getInterfaceNode(node);
        } else {
            throw new RuntimeException("not in class or interface node ");
        }

        List<ParseTreeNode> className = new ParseTreeNodeFinder.Builder().setFirst(true).create()
                .getNeededNodes(classNode, DECLARATION_DEFINE_TYPE_NAME);
        if (!CollectionsUtils.isSingleItem(className)) {
            throw new RuntimeException("can not get class node ");
        }
        return CollectionsUtils.getFirstItem(className).getLabel();
    }

    /**
     * 判断是否是interface 节点
     *
     * @param node
     * @return
     */
    public static boolean isInInterfaceNode(@NotNull ParseTreeNode node) {
        List<ParseTreeNode> nodes = new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create()
                .getNeededNodes(node, DECLARATION_DEFINE_INTERFACE);
        return nodes.size() != 0;
    }

    /**
     * 获取完整interface节点
     *
     * @param node
     * @return
     */
    public static ParseTreeNode getInterfaceNode(@NotNull ParseTreeNode node) {
        if (!isInInterfaceNode(node)) {
            throw new RuntimeException(" not in interface node");
        }
        List<ParseTreeNode> list =
                new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create().getNeededNodes(node,
                        DECLARATION_DEFINE_INTERFACE);

        if (!CollectionsUtils.isSingleItem(list)) {
            throw new RuntimeException("can not get getInterfaceNode ,because of the size of list is :" + list.size());
        }

        return CollectionsUtils.getFirstItem(list);
    }

    /**
     * 判断当前节点是否在CLASS节点
     *
     * @param node
     * @return
     */
    public static boolean isInClassNode(@NotNull ParseTreeNode node) {
        List<ParseTreeNode> nodes = new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create()
                .getNeededNodes(node, DECLARATION_CLASS);
        return nodes.size() != 0;
    }

    /**
     * 获取完整class节点
     *
     * @param node
     * @return
     */
    public static ParseTreeNode getClassNode(@NotNull ParseTreeNode node) {
        if (!isInClassNode(node)) {
            throw new RuntimeException(" not in class node");
        }
        List<ParseTreeNode> list =
                new ParseTreeNodeFinder.Builder().setReverse(true).setFirst(true).create().getNeededNodes(node,
                        DECLARATION_CLASS);
        if (!CollectionsUtils.isSingleItem(list)) {
            throw new RuntimeException("can not get get classNode ,because of the size of list is :" + list.size());
        }

        return CollectionsUtils.getFirstItem(list);
    }

}
