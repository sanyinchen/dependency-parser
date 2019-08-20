
package com.sanyinchen.parser.tree;


import com.sanyinchen.parser.lexer.exceptions.ParseTreeProcessorException;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * AST树序列化工具
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public enum ParseTreeSerializer {

    INSTANCE;

    private String getStringForEdge(ParseTreeNode c) {
        return "\tn" + c.getParent().getId() + " -- n" + c.getId() + ";\n";
    }

    private String getStringForNode(ParseTreeNode n) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tn");
        sb.append(n.getId());
        sb.append(" [label=\"");

        // terminal nodes
        if (n.isTerminal()) {
            sb.append(n.getEscapedLabel());
        } else {
            sb.append(n.getRule());
        }
        if (n.isConflicted()) {
            sb.append("1_cf_id_" + n.getStatus().getConflictId().getValue());
        } else {
            sb.append(0);
        }
        sb.append("\",");

        // terminal nodes
        if (!n.isTerminal()) {
            sb.append("shape=ellipse");
        } else {
            sb.append("shape=box");
        }
        // set color
        if (n.isConflicted()) {
            sb.append(",color=\"red\"");
        }else if (!n.isValid()){
            sb.append(",color=\"brown\"");
        }
        sb.append("];\n");

        return sb.toString();
    }


    public void toDotRec(StringBuilder sb, ParseTreeNode par) {

        sb.append(getStringForNode(par));

        for (ParseTreeNode cc : par.getChildren()) {
            toDotRec(sb, cc);
            sb.append(getStringForEdge(cc));
        }
    }

    public String toDot(ParseTree parseTree) {
        StringBuilder sb = new StringBuilder()
                .append("graph {\n")
                .append("\tnode [fontname=Helvetica,fontsize=11];\n")
                .append("\tedge [fontname=Helvetica,fontsize=10];\n");
        toDotRec(sb, parseTree.getRoot());
        sb.append("}\n");

        return sb.toString();
    }

    public String toJson(ParseTree parseTree) throws ParseTreeProcessorException {
        JsonProcessor jsonProc = new JsonProcessor(parseTree);
        String sb = null;
        sb = jsonProc.process().toString();

        return sb;
    }



}
