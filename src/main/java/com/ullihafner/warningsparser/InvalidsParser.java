package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for Oracle Invalids.
 *
 * @author Ulli Hafner
 */
public class InvalidsParser extends RegexpLineParser {
    static final String WARNING_PREFIX = "Oracle ";
    private static final String INVALIDS_PATTERN = "^\\s*(\\w+),([a-zA-Z#_0-9/]*),([A-Z_ ]*),(.*),(\\d+),\\d+,([^:]*):\\s*(.*)$";

    /**
     * Creates a new instance of {@link InvalidsParser}.
     */
    public InvalidsParser() {
        super(INVALIDS_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String type = WARNING_PREFIX + StringUtils.capitalize(StringUtils.lowerCase(matcher.group(4)));
        String category = matcher.group(6);
        Priority priority;
        if (StringUtils.contains(category, "PLW-07")) {
            priority = Priority.LOW;
        }
        else if (StringUtils.contains(category, "ORA")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        Warning warning = new Warning(matcher.group(2) + "." + matcher.group(3), getLineNumber(matcher.group(5)), type,
                category, matcher.group(7), priority);
        warning.setPackageName(matcher.group(1));

        return warning;
    }
}
