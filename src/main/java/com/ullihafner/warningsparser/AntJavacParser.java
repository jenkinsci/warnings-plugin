package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import hudson.Extension;

/**
 * A parser for the ant javac compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class AntJavacParser extends RegexpLineParser {

    /** Pattern of javac compiler warnings. */
    private static final String ANT_JAVAC_WARNING_PATTERN = ANT_TASK
            + "\\s*(.*java):(\\d*):\\s*(?:warning|\u8b66\u544a)\\s*:\\s*(?:\\[(\\w*)\\])?\\s*(.*)$"
            + "|^\\s*\\[.*\\]\\s*warning.*\\]\\s*(.*\"(.*)\".*)$";

    // \u8b66\u544a is Japanese l10n

    /**
     * Creates a new instance of {@link AntJavacParser}.
     */
    public AntJavacParser() {
        super(ANT_JAVAC_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("\u8b66\u544a");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        if (StringUtils.isBlank(matcher.group(5))) {
            String message = matcher.group(4);
            String category = classifyIfEmpty(matcher.group(3), message);
            return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message);
        }
        else {
            return createWarning(matcher.group(6), 0, "Path", matcher.group(5));
        }
    }

}
