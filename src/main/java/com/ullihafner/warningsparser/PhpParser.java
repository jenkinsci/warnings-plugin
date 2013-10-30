package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for PHP runtime errors and warnings.
 *
 * @author Shimi Kiviti
 */
public class PhpParser extends RegexpLineParser {
    public static final String FATAL_ERROR_CATEGORY = "PHP Fatal error";
    public static final String WARNING_CATEGORY = "PHP Warning";
    public static final String NOTICE_CATEGORY = "PHP Notice";
    private static final String PHP_WARNING_PATTERN = "^.*(PHP Warning|PHP Notice|PHP Fatal error):\\s+(.+ in (.+) on line (\\d+))$";

    /**
     * Creates a new instance of {@link PhpParser}.
     */
    public PhpParser() {
        super(PHP_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("PHP");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String category = matcher.group(1);
        String message = matcher.group(2);
        String fileName = matcher.group(3);
        String start = matcher.group(4);

        Priority priority = Priority.NORMAL;

        if (category.contains("Fatal")) {
            priority = Priority.HIGH;
        }

        return createWarning(fileName, Integer.parseInt(start), category, message, priority);
    }
}