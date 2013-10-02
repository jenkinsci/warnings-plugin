package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Inter C compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
public class IntelCParser extends RegexpLineParser {
    private static final String INTEL_PATTERN = "^(.*)\\((\\d*)\\)?:(?:\\s*\\(col\\. (\\d+)\\))?.*((?:remark|warning|error)\\s*#*\\d*)\\s*:\\s*(.*)$";

    /**
     * Creates a new instance of {@link IntelCParser}.
     */
    public IntelCParser() {
        super(INTEL_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("warning") || line.contains("error") || line.contains("remark");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String category = StringUtils.capitalize(matcher.group(4));

        Priority priority;
        if (StringUtils.startsWith(category, "Remark")) {
            priority = Priority.LOW;
        }
        else if (StringUtils.startsWith(category, "Error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        Warning warning = createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, matcher.group(5),
                priority);
        warning.setColumnPosition(getLineNumber(matcher.group(3)));
        return warning;
    }
}
