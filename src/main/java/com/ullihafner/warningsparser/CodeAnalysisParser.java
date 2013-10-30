package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the CodeAnalysis compiler warnings.
 *
 * @author Rafal Jasica
 */
public class CodeAnalysisParser extends RegexpLineParser {
    private static final String WARNING_PATTERN = ANT_TASK
            + "((MSBUILD)|((.+)\\((\\d+)\\)))\\s*:\\s*[Ww]arning\\s*:\\s*(\\w*)\\s*:\\s*(Microsoft\\.|)(\\w*(\\.\\w*)*)\\s*:\\s*(.*)\\[(.*)\\]\\s*$";

    /**
     * Creates a new instance of {@link CodeAnalysisParser}.
     */
    public CodeAnalysisParser() {
        super(WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        if (StringUtils.isNotBlank(matcher.group(2))) {
            return createWarning(matcher.group(11), 0, matcher.group(6), matcher.group(8), matcher.group(10),
                    Priority.NORMAL);
        }
        else {
            return createWarning(matcher.group(4), getLineNumber(matcher.group(5)), matcher.group(6), matcher.group(8),
                    matcher.group(10), Priority.NORMAL);
        }
    }
}
