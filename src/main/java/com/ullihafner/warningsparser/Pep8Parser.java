package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Pep8 compiler warnings.
 *
 * @author Marvin Schütz
 */
public class Pep8Parser extends RegexpLineParser {
    private static final String PEP8_WARNING_PATTERN = "(.*):(\\d+):(\\d+): (\\D\\d*) (.*)";

    /**
     * Creates a new instance of {@link Pep8Parser}.
     */
    public Pep8Parser() {
        super(PEP8_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(5);
        String category = classifyIfEmpty(matcher.group(4), message);

        Warning warning = createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message,
                mapPriority(category));
        warning.setColumnPosition(getLineNumber(matcher.group(3)));
        return warning;
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(":");
    }

    private Priority mapPriority(final String priority) {
        if (priority.contains("F") || priority.contains("E") || priority.contains("W")) {
            return Priority.HIGH;
        }
        else if (priority.contains("R")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }
}
