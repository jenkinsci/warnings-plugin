package hudson.plugins.warnings.parser;

import hudson.plugins.analysis.util.model.Priority;

import java.util.regex.Matcher;

/**
 * A generic parser for the warning format:
 * [<warnigType>] - <fileName>:<lineNumber>:<category>:<priority>:<message>
 *
 * @author Andreas Lüthi
 */
public class GenericParser extends RegexpLineParser {
    /** Pattern of ACSXtext compiler warnings. */
    private static final String GENERIC_PARSER_PATTERN = "^\\[(.*)\\] \\- \\s*(.*):(\\d+):(.*?):(.*?):\\s*(.*?)$";

    public GenericParser() {
        super(GENERIC_PARSER_PATTERN, "Generic Parser");
    }

    /** {@inheritDoc} */
    @Override
    protected Warning createWarning(final Matcher matcher) {
        String warnigType = matcher.group(1);
        String fileName = matcher.group(2);
        String lineNumber = matcher.group(3);
        String category = matcher.group(4);
        String prio = matcher.group(5);
        String message = matcher.group(6);
        Priority priority = Priority.fromString(prio);
        return new Warning(fileName, Integer.parseInt(lineNumber), warnigType, category, message,
                priority);
    }

}
