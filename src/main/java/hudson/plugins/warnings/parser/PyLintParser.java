package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.plugins.analysis.util.model.Priority;

import hudson.Extension;

/**
 * A parser for the PyLint compiler warnings.
 *
 * @author Sebastian Hansbauer
 */
@Extension
public class PyLintParser extends RegexpLineParser {
    private static final long serialVersionUID = 4464053085862883240L;

    /**
     * Parses lines of the type expected from PyLint parseable output.
     * <br>
     * Example line: <br>
     * <pre>app/__init__.py:202: [(low) C0326(bad-whitespace), ] No space allowed around keyword argument assignment</pre>
     * <br>
     * The priority, eg (low), is optional.  If this is not included, then
     * E/F/W defaults to high and everything else is normal priority.<br>
     */
    private static final String PYLINT_ERROR_PATTERN = "(.*):(\\d+): \\[(?:\\((\\w+)\\))?\\s*(\\D\\d*).*\\] (.*)";

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
        String message = matcher.group(5);
        String category = classifyIfEmpty(matcher.group(4), message);
        Priority priority = null;
        if (matcher.group(3) != null) {
            try {
                priority = Priority.fromString(matcher.group(3));
            } catch (IllegalArgumentException e) {
                // will fall back to E/F/W = high
            }
        }
        if (priority == null) {
            //First letter of the Pylint classification is one of F/E/W/R/C. E/F/W are high priority.
            priority = Priority.NORMAL;
            if (category.charAt(0) == 'E' || category.charAt(0) == 'F' || category.charAt(0) == 'W') {
                priority = Priority.HIGH;
            }
        }
        
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message, priority);
    }
}
