package com.sanyinchen.demo;

import com.sanyinchen.parser.CodeDependencyParser;

/**
 * Created by sanyinchen on 19-8-20.
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-8-20
 */

public class FooFullJar {
    public static void main(String[] args) {
        CodeDependencyParser codeDependencyParser = new CodeDependencyParser();
        codeDependencyParser.parse("./input-test/lib-a-1.0-SNAPSHOT.jar","./input-test/lib-b-1.0-SNAPSHOT.jar");
    }
}
