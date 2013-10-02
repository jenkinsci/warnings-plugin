package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

/**
 * A line parser that uses a configurable regular expression and Groovy script to parse warnings.
 *
 * @author Ulli Hafner
 */
public class DynamicParser extends RegexpLineParser {
    private final GroovyExpressionMatcher expressionMatcher;

    /**
     * Creates a new instance of {@link DynamicParser}.
     *
     * @param regexp
     *            regular expression
     * @param script
     *            Groovy script
     */
    public DynamicParser(final String regexp, final String script) {
        super(regexp);

        expressionMatcher = new GroovyExpressionMatcher(script, FALSE_POSITIVE);
    }

    /**
     * Creates a new annotation for the specified pattern.
     *
     * @param matcher
     *            the regular expression matcher
     * @return a new annotation for the specified pattern
     */
    @Override
    protected Warning createWarning(final Matcher matcher) {
        return expressionMatcher.createWarning(matcher);
    }
}
