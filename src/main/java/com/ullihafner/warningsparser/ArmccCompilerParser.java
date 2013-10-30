package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for armcc compiler warnings.
 *
 * @author Emanuele Zattin
 */
public class ArmccCompilerParser extends RegexpLineParser {

    private static final String ARMCC_WARNING_PATTERN = "^\"(.+)\", line (\\d+): ([A-Z][a-z]+):\\D*(\\d+)\\D*?:\\s+(.+)$";

    /**
     * Creates a new instance of {@link ArmccCompilerParser}.
     */
    public ArmccCompilerParser() {
        super(ARMCC_WARNING_PATTERN);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        String type = matcher.group(3);
        int errorCode = getLineNumber(matcher.group(4));
        String message = matcher.group(5);
        Priority priority;

        if ("error".equalsIgnoreCase(type)) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return createWarning(fileName, lineNumber, errorCode + " - " + message, priority);
    }
}
