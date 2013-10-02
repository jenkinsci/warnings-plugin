package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the gcc compiler warnings.
 *
 * @author Greg Roth
 */
public class GccParser extends RegexpLineParser {
    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";
    private static final String GCC_WARNING_PATTERN = "^(?:\\s*(?:\\[.*\\]\\s*)?(.*\\.[chpimxsola0-9]+):(?:(\\d*):(?:\\d*:)*\\s*(?:(warning|error|note)\\s*:|\\s*(.*))|\\s*(undefined reference to.*))(.*)|.*ld:\\s*(.*-l(.*)))$";

    /**
     * Creates a new instance of {@link GccParser}.
     */
    public GccParser() {
        super(GCC_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        if (StringUtils.isNotBlank(matcher.group(7))) {
            return createWarning(matcher.group(8), 0, LINKER_ERROR, matcher.group(7), Priority.HIGH);
        }
        Priority priority;
        String fileName = matcher.group(1);
        if (StringUtils.contains(fileName, "cleartool")) {
            return FALSE_POSITIVE;
        }
        if ("warning".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.NORMAL;
        }
        else if ("error".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.HIGH;
        }
        else if ("note".equalsIgnoreCase(matcher.group(3))) {
            priority = Priority.LOW;
        }
        else if (StringUtils.isNotBlank(matcher.group(4))) {
            if (matcher.group(4).contains("instantiated from here")) {
                return FALSE_POSITIVE;
            }
            return createWarning(fileName, getLineNumber(matcher.group(2)), GCC_ERROR, matcher.group(4), Priority.HIGH);
        }
        else {
            return createWarning(fileName, 0, GCC_ERROR, matcher.group(5), Priority.HIGH);
        }
        String category = "GCC " + matcher.group(3);
        return createWarning(fileName, getLineNumber(matcher.group(2)), category, matcher.group(6), priority);
    }
}
