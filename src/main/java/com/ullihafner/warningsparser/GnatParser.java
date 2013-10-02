package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Gnat compiler warnings.
 *
 * @author Bernhard Berger
 */
public class GnatParser extends RegexpLineParser {
    private static final String GNAT_WARNING_PATTERN = "^(.+.(?:ads|adb)):(\\d+):(\\d+): ((?:error:)|(?:warning:)|(?:\\(style\\))) (.+)$";

    /**
     * Creates a new instance of {@link GnatParser}.
     */
    public GnatParser() {
        super(GNAT_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        String category;

        if ("warning:".equalsIgnoreCase(matcher.group(4))) {
            priority = Priority.NORMAL;
            category = "GNAT warning";
        }
        else if ("(style)".equalsIgnoreCase(matcher.group(4))) {
            priority = Priority.LOW;
            category = "GNAT style";
        }
        else {
            priority = Priority.HIGH;
            category = "GNAT error";
        }
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, matcher.group(5), priority);
    }
}
