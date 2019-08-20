

package com.sanyinchen.parser.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static com.sanyinchen.parser.tree.BasicTree.BUILD_CODE_RANGE;
import static com.sanyinchen.parser.tree.BasicTree.BUILD_LABEL;
import static com.sanyinchen.parser.tree.BasicTree.BUILD_LINE_RANGE;
import static com.sanyinchen.parser.tree.BasicTree.BUILD_NODE_CHILD;
import static com.sanyinchen.parser.tree.BasicTree.BUILD_NODE_TYPE;
import static com.sanyinchen.parser.tree.BasicTree.DECLARATION_CONFLICT;
import static com.sanyinchen.parser.tree.BasicTree.DECLARATION_CONFLICT_ID;
import static com.sanyinchen.parser.tree.BasicTree.DECLARATION_STATUS_CODE;
/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * JSON 序列化输出解析器
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */

public class JsonProcessor extends ParseTreeProcessor<JsonObject, JsonObject> {


    /**
     * constructor
     *
     * @param parseTree abstract syntax tree to process
     */
    public JsonProcessor(ParseTree parseTree) {
        super(parseTree);
    }

    @Override
    public JsonObject getResult() {
        return smap.get(parseTree.getRoot());
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void process(ParseTreeNode n) {

        if (!n.hasParent()) {
            simpleProp(n);
            return;
        }

        JsonObject nodeObj = new JsonObject();

        nodeObj.addProperty(BUILD_NODE_TYPE, n.getRule());
        JsonObject status = new JsonObject();
        status.addProperty(DECLARATION_CONFLICT_ID, n.getStatus().getConflictId().getValue());
        status.addProperty(DECLARATION_STATUS_CODE, n.getStatus().getStatusCode());

        nodeObj.add(DECLARATION_CONFLICT, status);
        nodeObj.addProperty(BUILD_CODE_RANGE, n.getSidx() + "," + n.getEidx());
        nodeObj.addProperty(BUILD_LINE_RANGE, n.getSline() + "," + n.getEline());
        nodeObj.addProperty(BUILD_LABEL, n.getEscapedLabel());


        if (n.hasChildren()) {
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < n.getChildren().size(); i++) {
                jsonArray.add(smap.get(n.getChild(i)));
            }
            nodeObj.add(BUILD_NODE_CHILD, jsonArray);
        }


        smap.put(n, nodeObj);
    }
}
