package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import hudson.Extension;

/**
 * A parser for the Acu Cobol compile.
 *
 * @author jerryshea
 */
@Extension
public class AcuCobolParser extends RegexpLineParser {
    private static final String ACUCOBOL_WARNING_PATTERN = "^\\s*(\\[.*\\])?\\s*?(.*), line ([0-9]*): Warning: (.*)$";

    /**
     * Creates a new instance of {@link AcuCobolParser}.
     */
    public AcuCobolParser() {
        super(ACUCOBOL_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(4);
        String category = classifyWarning(message);
        return new Warning(matcher.group(2), getLineNumber(matcher.group(3)), category, message);
    }
}
