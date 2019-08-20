
package com.sanyinchen.parser.util;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * 格式化输出字符串，过滤某些非法字符
 * escaping helper class
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public final class EscapeUtils {

    private static final Set<Character> SPECIAL_V1 = Stream.of('.').collect(toSet());
    private static final Set<Character> SPECIAL_V2 = Stream.of('+', '{', '}', '(', ')', '[', ']', '&', '^',
            '-', '?', '*', '\"', '$', '<', '>', '|', '#').collect(toSet());

    private EscapeUtils() {
    }

    /**
     * escape special character in a string with a backslash
     *
     * @param s string to be escaped
     * @return escaped string
     */
    public static String escapeSpecialCharacters(String s) {
        if (s == null) {
            return "";
        }
//    return StringEscapeUtils.escapeJava(s);
        StringBuilder out = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (SPECIAL_V1.contains(c)) {
                out.append("\\\\").append(c);
            } else if (SPECIAL_V1.contains(c)) {
                out.append("\\").append(c);
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }


    /**
     * unescape special character in a string
     *
     * @param s string to be unescaped
     * @return unescaped string
     */
    public static String unescapeSpecialCharacters(String s) {
        if (s == null) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        char pred = ' ';
        for (char c : s.toCharArray()) {
            if (pred == '\\' && (SPECIAL_V1.contains(c) || SPECIAL_V2.contains(c))) {
                out.deleteCharAt(out.length() - 1);
                out.append(c);
            } else {
                out.append(c);
            }
            pred = c;
        }
        return out.toString();
    }
}