package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

/**
 * A parser for the ant JavaDoc compiler warnings.
 *
 * @author Ulli Hafner
 */
public class JavaDocParser extends RegexpLineParser {
    private static final String JAVA_DOC_WARNING_PATTERN = "(?:\\s*\\[(?:javadoc|WARNING)\\]\\s*)?(?:(?:(.*):(\\d+))|(?:\\s*javadoc\\s*)):\\s*warning\\s*-\\s*(.*)";

    /**
     * Creates a new instance of {@link JavaDocParser}.
     */
    public JavaDocParser() {
        super(JAVA_DOC_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(3);
        String fileName = StringUtils.defaultIfEmpty(matcher.group(1), " - ");

        return createWarning(fileName, getLineNumber(matcher.group(2)), message);
    }
}
