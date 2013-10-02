package com.ullihafner.warningsparser;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * Parses an input stream for compiler warnings and returns the found warnings. If your parser is based on a regular
 * expression you can extend from the existing base classes {@link RegexpLineParser} or {@link RegexpDocumentParser}.
 *
 * @see RegexpLineParser Parses files line by line
 * @see RegexpDocumentParser Parses files using mulit-line regular expression
 * @see GccParser example
 * @see JavacParser example
 * @author Ulli Hafner
 * @since 4.0
 */
public abstract class AbstractWarningsParser implements WarningsParser {

    /**
     * Parses the specified input stream for compiler warnings and returns the found annotations. Note that the
     * implementor of this method must not close the given reader, this is done by the framework.
     *
     * @param reader
     *            the reader to get the text from
     * @return the collection of annotations
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws ParsingCanceledException
     *             Signals that the user canceled this operation
     */
    public abstract Collection<Warning> parse(final Reader reader) throws IOException, ParsingCanceledException;

    /**
     * Creates a new instance of {@link Warning} using the parser's group as warning type.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @return the warning
     */
    public Warning createWarning(final String fileName, final int start, final String category, final String message) {
        return new Warning(fileName, start, category, message);
    }

    /**
     * Creates a new instance of {@link Warning} using the parser's group as warning type. No category will be set.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param message
     *            the message of the warning
     * @return the warning
     */
    public Warning createWarning(final String fileName, final int start, final String message) {
        return createWarning(fileName, start, StringUtils.EMPTY, message);
    }

    /**
     * Creates a new instance of {@link Warning} using the parser's group as warning type.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     * @return the warning
     */
    public Warning createWarning(final String fileName, final int start, final String category, final String message,
            final Priority priority) {
        return new Warning(fileName, start, category, message, priority);
    }

    /**
     * Creates a new instance of {@link Warning}.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param type
     *            the type of warning
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     * @return the warning
     * @since 4.24
     */
    public Warning createWarning(final String fileName, final int start, final String type, final String category,
            final String message, final Priority priority) {
        return new Warning(fileName, start, type, category, message, priority);
    }

    /**
     * Creates a new instance of {@link Warning} using the parser's group as warning type. No category will be set.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     * @return the warning
     */
    public Warning createWarning(final String fileName, final int start, final String message, final Priority priority) {
        return createWarning(fileName, start, StringUtils.EMPTY, message, priority);
    }

    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates a warning at the top of the file.
     *
     * @param lineNumber
     *            the line number (as a string)
     * @return the line number
     */
    protected final int getLineNumber(final String lineNumber) {
        return convertLineNumber(lineNumber);
    }

    /**
     * Converts a string line number to an integer value. If the string is not a valid line number, then 0 is returned
     * which indicates a warning at the top of the file.
     *
     * @param lineNumber
     *            the line number (as a string)
     * @return the line number
     * @since 4.37
     */
    public static int convertLineNumber(final String lineNumber) {
        if (StringUtils.isNotBlank(lineNumber)) {
            try {
                return Integer.parseInt(lineNumber);
            }
            catch (NumberFormatException exception) {
                // ignore and return 0
            }
        }
        return 0;
    }
}
