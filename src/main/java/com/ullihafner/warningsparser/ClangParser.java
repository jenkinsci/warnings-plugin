package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Clang compiler warnings.
 *
 * @author Neil Davis
 */
public class ClangParser extends RegexpLineParser {
    private static final String CLANG_WARNING_PATTERN = "^\\s*(?:\\d+%)?([^%]*?):(\\d+):(?:(\\d+):)?(?:(?:\\{\\d+:\\d+-\\d+:\\d+\\})+:)?\\s*(warning|.*error):\\s*(.*?)(?:\\[(.*)\\])?$";

    /**
     * Creates a new instance of {@link ClangParser}.
     */
    public ClangParser() {
        super(CLANG_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String filename = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        int column = getLineNumber(matcher.group(3));
        String type = matcher.group(4);
        String message = matcher.group(5);
        String category = matcher.group(6);

        Priority priority;
        if (type.contains("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        Warning warning;
        if (category == null) {
            warning = createWarning(filename, lineNumber, message, priority);
        }
        else {
            warning = createWarning(filename, lineNumber, category, message, priority);
        }
        warning.setColumnPosition(column);
        return warning;
    }
}
