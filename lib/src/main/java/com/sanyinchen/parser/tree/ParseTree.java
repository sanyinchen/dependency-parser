
package com.sanyinchen.parser.tree;

import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sun.istack.internal.NotNull;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * AST抽象树基础数据结构
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */

public class ParseTree {

    private final ParseTreeNode root;
    List<ParseTreeNode> nodes = new ArrayList<>();

    /**
     * constructor
     * <p>
     * create a new abstract syntax tree
     *
     * @param nt    name of root non-terminal node
     * @param label value of root non-terminal node
     */
    public ParseTree(String nt, String label) {
        root = newNode(null, nt, label, 0, 0, 0, 0
                , ParseTreeNodeStatus.createNormalStatus());
    }

    /**
     * constructor
     * <p>
     * copy constructor
     *
     * @param tree tree to be duplicated
     */
    public ParseTree(ParseTree tree) {
        root = newNode(tree.root);
    }

    public static ParseTree deepCopy(@NotNull ParseTree tree) {
        return new ParseTree(tree);
    }

    /**
     * constructor
     *
     * @param nod root node
     */
    private ParseTree(ParseTreeNode nod) {
        root = newNode(nod);
    }

    /**
     * get root node
     *
     * @return root node
     */
    public ParseTreeNode getRoot() {
        return root;
    }

    /**
     * create new ast node
     *
     * @param parent root of new ast node to be created
     * @return newly created ast node
     */
    private ParseTreeNode newNode(ParseTreeNode parent) {
        ParseTreeNode rn = new ParseTreeNode(this, parent);
        nodes.add(rn);
        return rn;
    }

    /**
     * create new ast node
     *
     * @param parent parent node
     * @param nt     name of node to be created
     * @param label  value of node to be created
     * @param sidx   start index
     * @param eidx   end index
     * @return newly created node
     */
    public ParseTreeNode newNode(ParseTreeNode parent, String nt, String label, int sidx,
                                 int eidx, int sline, int eline, ParseTreeNodeStatus status) {
        ParseTreeNode rn = new ParseTreeNode(this, parent, nt, label, sidx, eidx, sline, eline, status);
        nodes.add(rn);
        return rn;
    }

    /**
     * create new ast node
     *
     * @param parent
     * @param newNode
     * @return
     */
    public ParseTreeNode newNode(ParseTreeNode parent, ParseTreeNode newNode) {
        ParseTreeNode rn = new ParseTreeNode(this, parent, newNode.getRule(), newNode.getLabel(), newNode.getSidx(),
                newNode.getEidx(), newNode.getSline(), newNode.getEline(),
                newNode.getStatus());
        nodes.add(rn);
        return rn;
    }


    /**
     * get leaf nodes
     *
     * @return set of leaf nodes
     */
    public Set<ParseTreeNode> getLeafs() {
        return nodes.stream().filter(n -> !n.hasChildren()).collect
                (Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * get all nodes
     *
     * @return list of ast nodes
     */
    public List<ParseTreeNode> getNodes() {
        return nodes;
    }

    /**
     * generate dot representation from ast
     *
     * @return dot format string
     */
    public String toDot() {
        return ParseTreeSerializer.INSTANCE.toDot(this);
    }

    /**
     * generate JSON representation from ast
     *
     * @return JSON format string
     */
    public String toJson() throws ParseTreeProcessorException {

        return ParseTreeSerializer.INSTANCE.toJson(this);
    }



    /**
     * replace oldTree by newTree
     *
     * @param oldTree tree to be replaced
     * @param newTree tree replacement
     * @return true when subtree replacement was successful, false otherwise
     */
    public boolean replaceSubtree(ParseTree oldTree, ParseTree newTree) {
        if (hasSubtree(oldTree)) {
            nodes.stream()
                    .filter(oldTree.root::equals)
                    .forEach(n -> n.getParent().replaceChild(oldTree.root, newTree.root));
            nodes.addAll(newTree.nodes);
            return nodes.removeAll(oldTree.nodes);
        }
        return false;
    }

    /**
     * remove subtree from ast
     *
     * @param subtree to be removed
     * @return true when removal was succesful, false otherwise
     */
    public boolean removeSubtree(ParseTree subtree) {
        if (hasSubtree(subtree)) {
            nodes.stream()
                    .filter(subtree.root::equals)
                    .forEach(n -> n.getParent().delChild(n));
            return nodes.removeAll(subtree.nodes);
        }
        return false;
    }

    /**
     * find dominant subtrees, i.e., subtrees where the distance of the subtree root
     * node to the ast root node is minimal.
     *
     * @param p predicate to search for the dominating subtree root node
     * @return set of dominating subtrees
     */
    public Set<ParseTree> getDominatingSubtrees(Predicate<ParseTreeNode> p) {
        Set<ParseTreeNode> selected = new HashSet<>();
        searchDominatingNodes(root, selected, p);
        return getSubtrees(selected::contains);
    }

    /**
     * helper method for finding the dominating subtrees
     *
     * @param n        current root
     * @param selected set to keep track of visited nodes
     * @param p        predicate to search for the dominating subtree root node
     */
    private void searchDominatingNodes(ParseTreeNode n, Set<ParseTreeNode> selected, Predicate<ParseTreeNode> p) {
        if (p.test(n)) {
            selected.add(n);
        } else {
            n.getChildren().forEach(an -> searchDominatingNodes(an, selected, p));
        }
    }

    /**
     * get subtree with the root node identified by p
     *
     * @param p predicate for identifying the root node
     * @return set of ast nodes
     */
    public Set<ParseTree> getSubtrees(Predicate<ParseTreeNode> p) {
        return nodes.stream().filter(p).map(ParseTree::new).collect(toSet());
    }

    /**
     * check the presence of subree in the current tree
     *
     * @param subtree tree whose presence is checked
     * @return true if subtree is present in actual one, false otherwise
     */
    public boolean hasSubtree(ParseTree subtree) {
        Set<ParseTree> subtrees = getSubtrees(subtree.root::equals);
        return subtrees.stream().anyMatch(subtree::equals);
    }

    /**
     * get subtree
     *
     * @param subtree tree whose presence is checked
     * @return subree
     */
    public ParseTree getSubtree(ParseTree subtree) {
        Set<ParseTree> subtrees = getSubtrees(n -> n.equals(subtree.root));
        return subtrees.stream().filter(subtree::equals).findFirst().orElse(null);
    }

    @Override
    public int hashCode() {
        return root.getId();
    }


    /**
     * recursive helper function for sorting the tree topologically
     *
     * @param ns in/out list to keep sorted nodes
     * @param n  node to consider
     */
    private void topoSortRec(List<ParseTreeNode> ns, ParseTreeNode n) {

        ns.add(n);

        for (ParseTreeNode cc : n.getChildren()) {
            topoSortRec(ns, cc);
        }

    }

    public void topoSort() {
        List<ParseTreeNode> nods = new ArrayList<>();
        topoSortRec(nods, root);
        assert nods.size() == this.nodes.size();
        this.nodes = nods;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ParseTree)) {
            return false;
        }

        ParseTree parseTree = (ParseTree) o;
        // will recursively check AST nodes
        return root.equals(parseTree.root);
    }
}
