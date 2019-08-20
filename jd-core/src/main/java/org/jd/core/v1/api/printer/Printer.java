/*
 * Copyright (c) 2008-2019 Emmanuel Dupuy.
 * This project is distributed under the GPLv3 license.
 * This is a Copyleft license that gives the user the right to use,
 * copy and modify the code freely for non-commercial purposes.
 */

package org.jd.core.v1.api.printer;


public interface Printer {
    void start(int maxLineNumber, int majorVersion, int minorVersion);

    void end();

    void printText(String text);

    void printNumericConstant(String constant);

    void printStringConstant(String constant, String ownerInternalName);

    void printKeyword(String keyword);

    // Declaration & reference types
    int TYPE = 1;
    int FIELD = 2;
    int METHOD = 3;
    int CONSTRUCTOR = 4;
    int PACKAGE = 5;
    int MODULE = 6;

    void printDeclaration(int type, String internalTypeName, String name, String descriptor);

    void printReference(int type, String internalTypeName, String name, String descriptor, String ownerInternalName);

    void indent();

    void unindent();

    int UNKNOWN_LINE_NUMBER = 0;

    void startLine(int lineNumber);

    void endLine();

    void extraLine(int count);

    // Marker types
    int COMMENT = 1;
    int JAVADOC = 2;
    int ERROR = 3;
    int IMPORT_STATEMENTS = 4;

    void startMarker(int type);

    void endMarker(int type);

    class DefaultPrinter implements Printer {

        protected static final String TAB = "  ";
        protected static final String NEWLINE = "\n";

        protected int indentationCount = 0;
        protected StringBuilder sb = new StringBuilder();

        @Override
        public String toString() {
            return sb.toString();
        }

        @Override
        public void start(int maxLineNumber, int majorVersion, int minorVersion) {
        }

        @Override
        public void end() {
        }

        @Override
        public void printText(String text) {
            sb.append(text);
        }

        @Override
        public void printNumericConstant(String constant) {
            sb.append(constant);
        }

        @Override
        public void printStringConstant(String constant, String ownerInternalName) {
            sb.append(constant);
        }

        @Override
        public void printKeyword(String keyword) {
            sb.append(keyword);
        }

        @Override
        public void printDeclaration(int type, String internalTypeName, String name, String descriptor) {
            sb.append(name);
        }

        @Override
        public void printReference(int type, String internalTypeName, String name, String descriptor,
                                   String ownerInternalName) {
            sb.append(name);
        }

        @Override
        public void indent() {
            this.indentationCount++;
        }

        @Override
        public void unindent() {
            this.indentationCount--;
        }

        @Override
        public void startLine(int lineNumber) {
            for (int i = 0; i < indentationCount; i++) sb.append(TAB);
        }

        @Override
        public void endLine() {
            sb.append(NEWLINE);
        }

        @Override
        public void extraLine(int count) {
            while (count-- > 0) {
                sb.append(NEWLINE);
            }
        }

        @Override
        public void startMarker(int type) {
        }

        @Override
        public void endMarker(int type) {
        }

    }
}
