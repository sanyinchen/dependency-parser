package com.sanyinchen.parser.util;


import com.sun.istack.internal.NotNull;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * 函数式编成帮助类
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class SmartMethod {


    public interface ClearMethodInterface<T> {
        void call(T... args);

    }

    public interface ClearMethodWithResInterface<T, O> {
        O call(T... args);

    }

    /**
     * 无返回结果invoke方法
     *
     * @param runnable
     * @param args
     * @param <T>
     */
    public static <T> void invokeMethod(@NotNull ClearMethodInterface<T> runnable, @NotNull T... args) {
        runnable.call(args);
    }

    /**
     * 带有返回结果的invoke方法
     *
     * @param runnable
     * @param args
     * @param <T>
     * @param <O>
     * @return
     */
    public static <T, O> O invokeWithResMethod(@NotNull ClearMethodWithResInterface<T, O> runnable,
                                               @NotNull T... args) {
        return runnable.call(args);
    }
}
