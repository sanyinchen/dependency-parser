package com.sanyinchen.parser.util;


import com.sanyinchen.parser.lexer.exceptions.InjectionException;
import com.sanyinchen.parser.tree.ParseTree;
import com.sanyinchen.parser.tree.ParseTreeNode;

/**
 * Created by sanyinchen on 19-3-27.
 * <p>
 * 插入代码树节点
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-27
 */

public enum ParseTreeManipulator {

    INTANCE;

    private void expand(ParseTree orig, ParseTreeNode par,
                        ParseTreeNode toinj) {

        ParseTreeNode nn = orig.newNode(par, toinj);


        for (ParseTreeNode c : toinj.getChildren()) {
            expand(orig, nn, c);
        }

        par.addChild(nn);

    }

    private void remove(ParseTree orig, ParseTreeNode toinj) {
        for (ParseTreeNode c : toinj.getChildren()) {
            remove(orig, c);
        }
        orig.getNodes().remove(toinj);
    }


    /**
     * Inject a parse tree into another one
     *
     * @param rcv            the receiving parse tree
     * @param injectionPoint injection point that defines the node
     *                       after/before the tree should be injected
     * @param toinject       the tree to inject
     * @throws InjectionException parse tree could not be injected
     */
    public void inject(ParseTree rcv,
                       InjectionPointDetector injectionPoint,
                       ParseTree toinject) throws InjectionException {

        ParseTreeNode ip = injectionPoint.detect(rcv);

        if (ip == null || !ip.hasParent()) {
            return;
        }

        ParseTreeNode par = ip.getParent();

        ParseTreeNode nn = rcv.newNode(par, toinject.getRoot().getFirstChild());


        int pos = par.getChildren().indexOf(ip);


        switch (injectionPoint.getPosition()) {
            case BEFORE:
                par.getChildren().add(pos, nn);
                for (ParseTreeNode cc : toinject.getRoot().getFirstChild().getChildren()) {
                    expand(rcv, nn, cc);
                }
                break;
            case AFTER:
                par.getChildren().add(pos + 1, nn);
                for (ParseTreeNode cc : toinject.getRoot().getFirstChild().getChildren()) {
                    expand(rcv, nn, cc);
                }
                break;
            case REPLACED:
                par.getChildren().add(pos, nn);
                for (ParseTreeNode cc : toinject.getRoot().getFirstChild().getChildren()) {
                    expand(rcv, nn, cc);
                }
                for (ParseTreeNode cc : par.getChildren().get(pos + 1).getChildren()) {
                    remove(rcv, cc);
                }
                par.getChildren().remove(pos + 1);
                break;
            default:
                break;
        }


    }


}
