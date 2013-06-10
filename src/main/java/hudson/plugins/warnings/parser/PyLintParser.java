package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.Extension;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Sebastian Hansbauer
 */
@Extension
public class PyLintParser extends RegexpLineParser {

    private static final long serialVersionUID = 4464053085862883240L;

    private static final String PYLINT_ERROR_PATTERN = "(.*):(\\d+): \\[(\\D\\d*).*\\] (.*)";

    /**
     * Creates a new instance of {@link PyLintParser}.
     */
    public PyLintParser() {
        super(Messages._Warnings_PyLint_ParserName(),
                Messages._Warnings_PyLint_LinkName(),
                Messages._Warnings_PyLint_TrendName(),
                PYLINT_ERROR_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("[");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(4);
        String category = classifyIfEmpty(matcher.group(3), message);

        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message);
    }

    @Override
    protected String getId() {
    return "PyLint"; // old ID in serialization
    }
}
