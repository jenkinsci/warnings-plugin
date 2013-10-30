package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for C++ Lint compiler warnings.
 *
 * @author Ulli Hafner
 */
public class CppLintParser extends RegexpLineParser {
    private static final String PATTERN = "^\\s*(.*)\\s*[(:](\\d*)\\)?:\\s*(.*)\\s*\\[(.*)\\] \\[(.*)\\]$";

    /**
     * Creates a new instance of {@link CppLintParser}.
     */
    public CppLintParser() {
        super(PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority = mapPriority(matcher.group(5));

        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(4), matcher.group(3),
                priority);
    }

    private Priority mapPriority(final String priority) {
        int value = getLineNumber(priority);
        if (value >= 5) {
            return Priority.HIGH;
        }
        if (value >= 3) {
            return Priority.NORMAL;
        }
        return Priority.LOW;
    }
}
