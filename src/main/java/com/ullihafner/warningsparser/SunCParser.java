package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the SUN Studio C++ compiler warnings.
 *
 * @author Ulli Hafner
 */
public class SunCParser extends RegexpLineParser {
    private static final String SUN_CPP_WARNING_PATTERN = "^\\s*\"(.*)\"\\s*,\\s*line\\s*(\\d+)\\s*:\\s*(Warning|Error)\\s*(?:, \\s*(.*))?\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of <code>HpiCompileParser</code>.
     */
    public SunCParser() {
        super(SUN_CPP_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        if ("warning".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(4), matcher.group(5), priority);
    }
}

