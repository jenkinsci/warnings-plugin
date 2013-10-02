package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ulli Hafner
 */
public class JavacParser extends RegexpLineParser {
    private static final String JAVAC_WARNING_PATTERN = "^(?:\\[WARNING\\]\\s+)?([^\\[]*):\\[(\\d+)[.,;]*(\\d+)?\\]\\s*(?:\\[(\\w+)\\])?\\s*(.*)$";

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(JAVAC_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(5);
        String category = classifyIfEmpty(matcher.group(4), message);

        Warning warning = createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message);
        warning.setColumnPosition(getLineNumber(matcher.group(3)));
        return warning;
    }
}
