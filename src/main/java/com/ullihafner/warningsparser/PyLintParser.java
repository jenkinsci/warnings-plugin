package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Sebastian Hansbauer
 */
public class PyLintParser extends RegexpLineParser {
    private static final String PYLINT_ERROR_PATTERN = "(.*):(\\d+): \\[(\\D\\d*).*\\] (.*)";

    /**
     * Creates a new instance of {@link PyLintParser}.
     */
    public PyLintParser() {
        super(PYLINT_ERROR_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(4);
        String category = classifyIfEmpty(matcher.group(3), message);

        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message);
    }
}
