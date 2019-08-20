
package com.sanyinchen.parser.tree;


import com.sanyinchen.parser.util.EscapeUtils;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * AST树node节点
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class ParseTreeNode {

    /**
     * root节点到当前节点的压缩代码
     */
    private String label;
    /**
     * 当前节点的type类型
     */
    private String ntype;
    /**
     * 父节点
     */
    private ParseTreeNode parent;
    /**
     * 当前所在的代码树
     */
    private ParseTree tree;
    private int id;
    /**
     * label压缩代码所在起点
     */
    private int sidx = 0;

    /**
     * label压缩代码所在终点
     */
    private int eidx = 0;

    /**
     * 源码所在行数起点
     */
    private int sline = 0;
    /**
     * 源码所在行数终点
     */
    private int eline = 0;


    /**
     * 当前节点状态
     */
    private ParseTreeNodeStatus status;

    /**
     * 代码树子节点
     */
    private List<ParseTreeNode> children;
    private static int cnt = 0;


    /**
     * constructor
     *
     * @param tree tree to whom the node belongs to
     */
    private ParseTreeNode(ParseTree tree) {
        this.tree = tree;
        id = cnt++;
        children = new ArrayList<>();
    }

    /**
     * constructor
     *
     * @param tree   tree to whom the node belongs to
     * @param parent parent node
     * @param nt     non terminal id
     * @param sidx   start index
     * @param eidx   end index
     * @param label  label
     */
    protected ParseTreeNode(ParseTree tree, ParseTreeNode parent, String nt, String label,
                            int sidx, int eidx, int sline, int eline, ParseTreeNodeStatus status) {
        this(tree);
        ntype = nt;
        this.label = label;
        this.parent = parent;
        this.sidx = sidx;
        this.eidx = eidx;
        this.sline = sline;
        this.eline = eline;
        this.status = status;
    }

    /**
     * deep copy constructor
     *
     * @param tree tree to whom the node belongs to
     * @param nod  node to duplication
     */
    protected ParseTreeNode(ParseTree tree, ParseTreeNode nod) {
        this(tree, nod, false);
    }

    protected ParseTreeNode(ParseTree tree, ParseTreeNode nod, boolean isOrphanNode) {
        this(tree);
        id = nod.id;
        ntype = nod.ntype;
        label = nod.label;
        this.eidx = nod.eidx;
        this.sidx = nod.sidx;
        for (ParseTreeNode c : nod.children) {
            ParseTreeNode cnod = new ParseTreeNode(tree, c, isOrphanNode);
            cnod.parent = this;
            if (!isOrphanNode) {
                this.tree.nodes.add(cnod);
            }
            children.add(cnod);
        }
    }

    /**
     * get child with index i
     *
     * @param i the index of the child
     * @return child with index i
     */
    public ParseTreeNode getChild(int i) {
        if (i < 0 || i > children.size()) {
            throw new IllegalArgumentException("Index must be greater than or equal to zero and less than the " +
                    "children size");
        }

        return children.get(i);
    }

    /**
     * get last child (note that nodes are ordered in their appearance)
     *
     * @return last child
     */
    public ParseTreeNode getLastChild() {
        if (!children.isEmpty()) {
            return children.get(children.size() - 1);
        }
        return null;
    }

    /**
     * 更新当前节点的冲突状态
     *
     * @return
     */
    public ParseTreeNode updateNodeStatus(@NotNull ParseTreeNodeStatus status) {
        this.status = status;
        return this;
    }

    public void syncChildrenStatus() {
        syncChildrenStatus(this);
    }

    /**
     * 同步更新子节点
     *
     * @param node
     */
    private void syncChildrenStatus(@NotNull ParseTreeNode node) {
        if (node == null) {
            return;
        }
        for (ParseTreeNode item : node.getChildren()) {
            syncChildrenStatus(item);
        }
        node.updateNodeStatus(status);
    }

    public boolean isConflicted() {
        return status.isConflicted();
    }

    public ParseTreeNodeStatus getStatus() {
        return status;
    }


    /**
     * get first child (note that nodes are ordered in their appearance)
     *
     * @return first child
     */
    public ParseTreeNode getFirstChild() {
        if (!children.isEmpty()) {
            return children.get(0);
        }
        return null;
    }

    /**
     * set parent node
     *
     * @param par parent node
     */
    public void setParent(ParseTreeNode par) {
        parent = par;
    }

    /**
     * check if node has parent
     *
     * @return true if node has parent, false otherwise
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * get parent node
     *
     * @return parent node
     */
    public ParseTreeNode getParent() {
        return parent;
    }

    /**
     * check if node has children
     *
     * @return true if node has children, false otherwise
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * check if node has multiple child
     *
     * @return
     */
    public boolean hasMultChildren() {
        return !children.isEmpty() && children.size() > 1;
    }

    /**
     * check if node has only one child
     *
     * @return
     */
    public boolean hasOnlyOneChild() {
        return !children.isEmpty() && children.size() == 1;
    }

    /**
     * get List of children
     *
     * @return list of children
     */
    public List<ParseTreeNode> getChildren() {
        return children;
    }

    /**
     * append child node
     *
     * @param n child node to be added
     */
    public void addChild(ParseTreeNode n) {
        children.add(n);
    }

    public void addChilds(List<ParseTreeNode> nodes) {
        children.addAll(nodes);
    }

    /**
     * delete child node
     *
     * @param n child node to be deleted
     */
    public void delChild(ParseTreeNode n) {
        children.remove(n);
    }

    /**
     * replace child
     *
     * @param oldNode child to be replaced
     * @param newNode replacement
     */
    public void replaceChild(ParseTreeNode oldNode, ParseTreeNode newNode) {
        if (children.contains(oldNode)) {
            children.set(children.indexOf(oldNode), newNode);
            newNode.parent = this;
        }
    }

    /**
     * gt identifier
     *
     * @return id which identifies node uniquely
     */
    public int getId() {
        return id;
    }

    /**
     * get non-terminal rule of that node
     *
     * @return non-terminal rule
     */
    public String getRule() {
        return ntype;
    }

    /**
     * get label where special chars are escaped
     *
     * @return escaped label
     */
    public String getEscapedLabel() {
        return EscapeUtils.escapeSpecialCharacters(label);
    }

    public int getEline() {
        return eline;
    }

    public int getSline() {
        return sline;
    }

    /**
     * get start index
     *
     * @return start index
     */
    public int getSidx() {
        return sidx;
    }

    /**
     * get ent index
     *
     * @return end index
     */
    public int getEidx() {
        return eidx;
    }


    /**
     * check whether node is terminal
     *
     * @return true if node is a terminal node
     */
    public boolean isTerminal() {
        return isLeaf() && ntype.isEmpty();
    }

    /**
     * get label
     *
     * @return unescaped label
     */
    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParseTreeNode)) {
            return false;
        }

        ParseTreeNode n = (ParseTreeNode) o;
        return n.id == id && n.ntype.equals(ntype) &&
                n.label.equals(label) && children.equals(n.children);
    }


    @Override
    public String toString() {
        return id + " " + ntype + " " + label;
    }

    /**
     * check whether this node is a leaf node
     *
     * @return true if node has no children, false otherwise
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * judge if is a single line statement
     *
     * @return
     */
    public boolean isInSingleLine() {
        return getEline() == getSline();
    }

    public boolean isValid() {
        return status.isValid();
    }


    /**
     * 获取当前node所在树的根节点
     *
     * @return 根节点
     */
    public ParseTreeNode getRootNode() {
        return tree.getRoot();
    }

    /**
     * 获取当前node所在的tree
     *
     * @return
     */
    public ParseTree getTree() {
        return tree;
    }
}
