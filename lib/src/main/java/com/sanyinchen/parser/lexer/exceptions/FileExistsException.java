

package com.sanyinchen.parser.lexer.exceptions;

/**
 * Created by sanyinchen on 19-3-23.
 * <p>
 * this exception is thrown when the file is exist
 *
 * @author sanyinchen
 * @version v0.1
 * @since 19-3-23
 */
public class FileExistsException extends Exception {

    FileExistsException(String msg) {
        super(msg);
    }
}
