package com.sanyinchen.parser.util;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Created by sanyinchen on 19-4-12.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-4-12
 */

public class CollectionsUtils {

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isSingleItem(Collection<?> list) {
        return !isEmpty(list) && list.size() == 1;
    }

    @Nullable
    public static synchronized <T> T getFirstItem(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Iterator<T> iterator = collection.iterator();
        return iterator.next();
    }

    public static <T> List<T> arrayListAdd(List<T> list, T item) {
        if (list != null && !(list instanceof ArrayList)) {
            throw new UnsupportedOperationException("just support ArrayList");
        }
        if (list == null) {
            List<T> temp = new ArrayList<>();
            temp.add(item);
            return temp;
        }
        list.add(item);
        return list;
    }

    /**
     * collection中的元素是否包含string
     *
     * @param collections
     * @param target
     * @return
     */
    public static boolean collectionContainsString(List<String> collections, String target) {
        if (CollectionsUtils.isEmpty(collections)) {
            return false;
        }

        for (int i = 0; i < collections.size(); i++) {
            String item = collections.get(i);
            if (!TextUtils.isEmpty(item) && item.contains(target)) {
                return true;
            }
        }

        return false;
    }
}
