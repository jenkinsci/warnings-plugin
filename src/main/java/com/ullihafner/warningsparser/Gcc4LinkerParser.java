package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for gcc 4.x linker warnings.
 *
 * @author Frederic Chateau
 */
public class Gcc4LinkerParser extends RegexpLineParser {
    /** A GCC error. */
    static final String WARNING_CATEGORY = "GCC4 Linker Error";
    /** Pattern of gcc 4 linker warnings. */
    private static final String LINKER_WARNING_PATTERN = "^(?:(.+?)(?:(?::(?:(\\d+):)? (undefined reference to .*))|(?::?\\(\\.\\w+\\+0x[0-9a-fA-F]+\\)): (?:(warning): )?(.*))|.*/ld(?:\\.exe)?: (?:(warning): )?(.*))$";

    /**
     * Creates a new instance of <code>Gcc4LinkerParser</code>.
     */
    public Gcc4LinkerParser() {
        super(LINKER_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String fileName = StringUtils.defaultString(matcher.group(1));
        int lineNumber = getLineNumber(matcher.group(2));
        String message;
        Priority priority = Priority.LOW;

        if (StringUtils.isNotBlank(matcher.group(7))) {
            // link error in ld
            if (StringUtils.equalsIgnoreCase(matcher.group(6), "warning")) {
                priority = Priority.NORMAL;
            }
            else {
                priority = Priority.HIGH;
            }
            message = matcher.group(7);
        }
        else {
            // link error
            if (StringUtils.isNotBlank(matcher.group(3))) {
                // error of type "undefined reference..."
                message = matcher.group(3);
                priority = Priority.HIGH;
            }
            else {
                // generic linker error with reference to the binary section and
                // offset
                if (StringUtils.equalsIgnoreCase(matcher.group(4), "warning")) {
                    priority = Priority.NORMAL;
                }
                else {
                    priority = Priority.HIGH;
                }
                message = matcher.group(5);
                if (StringUtils.endsWith(message, ":")) {
                    return FALSE_POSITIVE;
                }
            }
        }
        return createWarning(fileName, lineNumber, WARNING_CATEGORY, message, priority);
    }
}
