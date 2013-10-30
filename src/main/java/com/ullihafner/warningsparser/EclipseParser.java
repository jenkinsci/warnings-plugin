package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ulli Hafner
 */
public class EclipseParser extends RegexpDocumentParser {
    private static final String ANT_ECLIPSE_WARNING_PATTERN = "\\[?(WARNING|ERROR)\\]?\\s*(?:in)?\\s*(.*)(?:\\(at line\\s*(\\d+)\\)|:\\[(\\d+),).*(?:\\r?\\n[^\\^]*)+(?:\\r?\\n(.*)([\\^]+).*)\\r?\\n(?:\\s*\\[.*\\]\\s*)?(.*)";

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(ANT_ECLIPSE_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String type = StringUtils.capitalize(matcher.group(1));
        Priority priority;
        if ("warning".equalsIgnoreCase(type)) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        Warning warning = createWarning(matcher.group(2), getLineNumber(getLine(matcher)), matcher.group(7), priority);

        int columnStart = StringUtils.defaultString(matcher.group(5)).length();
        int columnEnd = columnStart + matcher.group(6).length();
        warning.setColumnPosition(columnStart, columnEnd);

        return warning;
    }

    private String getLine(final Matcher matcher) {
        String eclipse34 = matcher.group(3);
        String eclipse38 = matcher.group(4);

        return StringUtils.defaultIfEmpty(eclipse34, eclipse38);
    }
}
