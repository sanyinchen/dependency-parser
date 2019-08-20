package com.sanyinchen.parser.tree;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sanyinchen.parser.util.SmartMethod;
import com.sanyinchen.parser.util.WakeUniqueIdCreator;
import com.sun.istack.internal.NotNull;

import java.io.StringReader;

/**
 * Created by sanyinchen on 19-4-26.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-4-26
 */

public class DefaultBasicTree extends BasicTree {

    private String originStr;

    public DefaultBasicTree() {
        super();
        this.originStr = "{}";
    }

    public String getOriginStr() {
        return originStr;
    }

    public DefaultBasicTree(@NotNull ParseTree parseTree) {
        super();
        this.parseTree = parseTree;
    }


    public DefaultBasicTree(String originStr) {
        super();
        this.originStr = originStr;
        JsonParser jsonParser = new JsonParser();
        JsonReader reader = new JsonReader(new StringReader(originStr));
        reader.setLenient(true);
        JsonObject jsonObject = jsonParser.parse(reader).getAsJsonObject();
        try {
            createTree(jsonObject);
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 深度优先遍历创建parent节点
     * 广度优先遍历创建child节点
     *
     * @param jsonObject
     */
    private void createTree(JsonObject jsonObject) {
        try {
            String nt = jsonObject.get(BUILD_NODE_TYPE).getAsString();
            String[] ran = jsonObject.get(BUILD_CODE_RANGE).getAsString().split(",");
            String[] lineRan = jsonObject.get(BUILD_LINE_RANGE).getAsString().split(",");
            String lbl = jsonObject.get(BUILD_LABEL).getAsString();
            JsonObject conflictJson = jsonObject.get(DECLARATION_CONFLICT).getAsJsonObject();

            ParseTreeNodeStatus status = SmartMethod.invokeWithResMethod(args -> {
                JsonObject innerStatusJson = args[0];
                int statusCode = innerStatusJson.get(DECLARATION_STATUS_CODE).getAsInt();
                int cfId = innerStatusJson.get(DECLARATION_CONFLICT_ID).getAsInt();
                return ParseTreeNodeStatus.createCustomizeStatus(WakeUniqueIdCreator.INS.genCustomizeId(cfId),
                        statusCode);
            }, conflictJson);


            JsonArray cld = SmartMethod.invokeWithResMethod(args -> {
                JsonArray tempCld = null;
                try {
                    JsonObject obj = args[0];
                    tempCld = obj.getAsJsonArray(BUILD_NODE_CHILD);
                } catch (Exception e) {

                }
                return tempCld == null ? new JsonArray() : tempCld;
            }, jsonObject);

            addNode(nt, lbl, Integer.valueOf(ran[0])
                    , Integer.valueOf(ran[1]), Integer.valueOf(lineRan[0]), Integer.valueOf(lineRan[1]),
                    status);

            for (int i = 0; i < cld.size(); i++) {
                JsonObject cldNode = cld.get(i).getAsJsonObject();
                createTree(cldNode);
                nodeptr = nodeptr.getParent();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Json error");
        }
    }
}
