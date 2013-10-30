package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

/**
 * A parser for Flex SDK compiler warnings.
 *
 * @author Vivien Tintillier
 */
public class FlexSDKParser extends RegexpLineParser {
    private static final String FLEX_SDK_WARNING_PATTERN = "^\\s*(?:\\[.*\\])?\\s*(.*\\.as|.*\\.mxml)\\((\\d*)\\):\\s*(?:col:\\s*\\d*\\s*)?(?:Warning)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link FlexSDKParser}.
     */
    public FlexSDKParser() {
        super(FLEX_SDK_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(3));
    }
}
