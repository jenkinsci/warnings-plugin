package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the tnsdl translator warnings.
 *
 * @author Shaohua Wen
 */
public class TnsdlParser extends RegexpLineParser {
    static final String WARNING_CATEGORY = "Error";
    private static final String TNSDL_WARNING_PATTERN = "^tnsdl((.*)?):\\(.*\\) (.*) \\((.*)\\):(.*)$";

    /**
     * Creates a new instance of {@link TnsdlParser}.
     */
    public TnsdlParser() {
        super(TNSDL_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("tnsdl");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String fileName = matcher.group(3);
        int lineNumber = getLineNumber(matcher.group(4));
        String message = matcher.group(5);
        Priority priority;

        if (matcher.group().contains("(E)")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return createWarning(fileName, lineNumber, WARNING_CATEGORY, message, priority);
    }
}

