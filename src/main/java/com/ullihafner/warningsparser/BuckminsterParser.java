package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for Buckminster compiler warnings.
 *
 * @author Johannes Utzig
 */
public class BuckminsterParser extends RegexpLineParser {
    private static final String BUCKMINSTER_WARNING_PATTERN = "^.*(Warning|Error): file (.*?)(, line )?(\\d*): (.*)$";

    /**
     * Creates a new instance of {@link BuckminsterParser}.
     */
    public BuckminsterParser() {
        super(BUCKMINSTER_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority = "Error".equalsIgnoreCase(matcher.group(1)) ? Priority.HIGH : Priority.NORMAL;
        return createWarning(matcher.group(2), getLineNumber(matcher.group(4)), classifyWarning(matcher.group(5)),
                matcher.group(5), priority);

    }
}
