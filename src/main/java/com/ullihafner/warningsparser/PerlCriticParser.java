package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Perl::Critic warnings.
 *
 * @author Mihail Menev, menev@hm.edu
 */
public class PerlCriticParser extends RegexpLineParser {
    private static final String PERLCRITIC_WARNING_PATTERN = "(?:(.*?):)?(.*)\\s+at\\s+line\\s+(\\d+),\\s+column\\s+(\\d+)\\.\\s*(?:See page[s]?\\s+)?(.*)\\.\\s*\\(?Severity:\\s*(\\d)\\)?";

    /**
     * Creates a new instance of {@link PerlCriticParser}.
     */
    public PerlCriticParser() {
        super(PERLCRITIC_WARNING_PATTERN, true);
    }

    /** {@inheritDoc} */
    @Override
    protected Warning createWarning(final Matcher matcher) {
        String filename;
        if (matcher.group(1) == null) {
            filename = "-";
        }
        else {
            filename = matcher.group(1);
        }

        String message = matcher.group(2);
        int line = getLineNumber(matcher.group(3));
        int column = getLineNumber(matcher.group(4));
        String category = matcher.group(5);
        Priority priority = checkPriority(Integer.parseInt(matcher.group(6)));

        Warning warning = createWarning(filename, line, category, message, priority);
        warning.setColumnPosition(column, column);
        return warning;
    }

    /**
     * Checks the severity level, parsed from the warning and return the priority level.
     *
     * @param priority
     *            the severity level of the warning.
     * @return the priority level.
     */
    private Priority checkPriority(final int priority) {
        if (priority < 2) {
            return Priority.LOW;
        }
        else if (priority < 4) {
            return Priority.NORMAL;
        }
        else {
            return Priority.HIGH;
        }
    }
}