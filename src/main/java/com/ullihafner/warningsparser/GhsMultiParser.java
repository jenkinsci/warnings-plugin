package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the GHS Multi compiler warnings.
 *
 * @author Joseph Boulos
 */
public class GhsMultiParser extends RegexpDocumentParser {
    private static final String GHS_MULTI_WARNING_PATTERN = "\\.(.*)\\,\\s*line\\s*(\\d+):\\s*(warning|error)\\s*([^:]+):\\s*(?m)([^\\^]*)\\s*\\^";

    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(GHS_MULTI_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        String type = StringUtils.capitalize(matcher.group(3));
        String category = matcher.group(4);
        String message = matcher.group(5);
        Priority priority;
        if ("warning".equalsIgnoreCase(type)) {
            priority = Priority.NORMAL;
        }
        else {
            priority = Priority.HIGH;
        }
        return createWarning(fileName, lineNumber, category, message, priority);
    }
}
