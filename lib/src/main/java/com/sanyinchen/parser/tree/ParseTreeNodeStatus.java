package com.sanyinchen.parser.tree;


import com.sanyinchen.parser.tree.annotation.IntDef;
import com.sanyinchen.parser.util.WakeUniqueIdCreator;
import com.sun.istack.internal.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by sanyinchen on 19-4-18.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-4-18
 */

public class ParseTreeNodeStatus {


    public static final int BASED_BYTE = 0B0000;

    /**
     * 有效状态
     */
    public static final int STATUS_VALID = 0B00010000;
    public static final int STATUS_NORMAL = STATUS_VALID | (BASED_BYTE | 0B1); // normal 00010001
    public static final int STATUS_CONFLICT = STATUS_VALID | (BASED_BYTE | 0B10); // 冲突节点 00010010

    /**
     * 无效状态
     */
    public static final int STATUS_INVALID = 0B00100000;
    public static final int STATUS_INVALID_DEPRECATED = STATUS_INVALID | (BASED_BYTE | 0B1); // 废弃节点 00100001

    @IntDef({STATUS_INVALID_DEPRECATED, STATUS_CONFLICT, STATUS_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusCode {
    }

    private int value;
    private WakeUniqueIdCreator.WakeUniqueId conflictId = WakeUniqueIdCreator.INS.emptyId();

    public ParseTreeNodeStatus(@StatusCode int code, @NotNull WakeUniqueIdCreator.WakeUniqueId conflictId) {
        this.value = code;
        this.conflictId = conflictId;
    }

    public ParseTreeNodeStatus(@StatusCode int code) {
        value = STATUS_NORMAL;
        this.value = code;
    }

    public boolean isValid() {
        return (STATUS_VALID & value) == STATUS_VALID;
    }

    public boolean isConflicted() {
        return (STATUS_CONFLICT & value) == STATUS_CONFLICT;
    }

    public WakeUniqueIdCreator.WakeUniqueId getConflictId() {
        return conflictId;
    }


    public int getStatusCode() {
        return value;
    }

    @StatusCode
    public static int getStatusCodeByValue(int value) {
        switch (value) {
            case STATUS_NORMAL:
                return STATUS_NORMAL;
            case STATUS_CONFLICT:
                return STATUS_CONFLICT;
            case STATUS_INVALID_DEPRECATED:
                return STATUS_INVALID_DEPRECATED;
            default:
                break;
        }
        return STATUS_INVALID;
    }

    /**
     * 创建通用状态
     *
     * @return
     */
    public static ParseTreeNodeStatus createNormalStatus() {
        return new ParseTreeNodeStatus(STATUS_NORMAL);
    }


    public static ParseTreeNodeStatus createCustomizeStatus(@NotNull WakeUniqueIdCreator.WakeUniqueId cfId,
                                                            int statusCode) {
        return new ParseTreeNodeStatus(getStatusCodeByValue(statusCode), cfId);
    }

}
