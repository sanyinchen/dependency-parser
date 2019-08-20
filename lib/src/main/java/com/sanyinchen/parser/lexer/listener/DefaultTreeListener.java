package com.sanyinchen.parser.lexer.listener;

import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;
import com.sanyinchen.parser.tree.ParseTree;
import com.sanyinchen.parser.tree.ParseTreeNode;
import com.sanyinchen.parser.tree.ParseTreeNodeStatus;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class DefaultTreeListener extends DefaultListener {


    private static final long serialVersionUID = 5637734678821255670L;

    protected ParseTree parseTree = null;
    protected ParseTreeNode nodeptr = null;
    protected Predicate<String> filter = null;


    /**
     * constructor
     */
    public DefaultTreeListener() {
        parseTree = new ParseTree("root", "root");
        nodeptr = parseTree.getRoot();
        this.filter = x -> !x.isEmpty();
    }


    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    /**
     * create AST node
     *
     * @param ctx
     */
    @Override
    public void enterEveryRule(ParserRuleContext ctx) {


        String rule = getRuleByKey(ctx.getRuleIndex());
        if (filter.test(rule)) {
            Token s = ctx.getStart();
            Token e = ctx.getStop();
            ParseTreeNode n = parseTree.newNode(nodeptr, rule, ctx.getText(),
                    s != null ? s.getStartIndex() : 0,
                    e != null ? e.getStopIndex() : 0, s.getLine(), e.getLine(),
                    ParseTreeNodeStatus.createNormalStatus());
            nodeptr.addChild(n);
            nodeptr = n;

        }
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        String rule = getRuleByKey(ctx.getRuleIndex());
        if (filter.test(rule)) {
            nodeptr = nodeptr.getParent();
        }
    }

    @Override
    public void reset() {
        parseTree = new ParseTree("root", "root");
        nodeptr = parseTree.getRoot();
    }

    /**
     * get ast
     *
     * @return ast
     */
    public ParseTree getParseTree() {
        return parseTree;
    }

    /**
     * get ast nodes
     *
     * @return set of ast nodes
     */
    public Set<ParseTreeNode> getNodes() {
        return new HashSet<>(parseTree.getNodes());
    }

    @Override
    public String toString() {
        try {
            return getParseTree().toJson();
        } catch (ParseTreeProcessorException e) {
            e.printStackTrace();
        }
        return "";
    }


}
