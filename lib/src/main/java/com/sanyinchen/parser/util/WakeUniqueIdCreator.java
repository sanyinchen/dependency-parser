package com.sanyinchen.parser.util;

/**
 * Created by sanyinchen on 19-4-10.
 * <p>
 * <p>
 * 伪UniqueId生成器
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-4-10
 */

public enum WakeUniqueIdCreator {
    INS;

    private static volatile int ID = 0;
    private static WakeUniqueId unknownId = new WakeUniqueId(-1);

    public WakeUniqueId gen() {
        return new WakeUniqueId(++ID);
    }

    public WakeUniqueId genCustomizeId(int id) {
        return new WakeUniqueId(id);
    }

    public static class WakeUniqueId {
        private final int id;

        // 禁止外部构造UniqueId对象
        private WakeUniqueId(int id) {
            this.id = id;
        }

        public int getValue() {
            return id;
        }

        public boolean isEmpty() {
            return id == -1;
        }

    }

    public WakeUniqueId emptyId() {
        return unknownId;
    }
}
