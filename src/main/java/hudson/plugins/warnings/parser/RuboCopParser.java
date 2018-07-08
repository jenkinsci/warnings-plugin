package hudson.plugins.warnings.parser;

import hudson.Extension;
import hudson.plugins.analysis.util.model.Priority;
import hudson.plugins.warnings.WarningsDescriptor;

import java.util.regex.Matcher;

/**
 * A parser for the ruboCop warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class RuboCopParser extends RegexpLineParser {
    static final String RUBOCOP_SMALL_ICON = WarningsDescriptor.IMAGE_PREFIX + "rubocop-24x24.png";
    static final String RUBOCOP_LARGE_ICON = WarningsDescriptor.IMAGE_PREFIX + "rubocop-48x48.png";

    private static final long serialVersionUID = 7199325311690082783L;
    private static final String RUBOCOP_WARNING_PATTERN = "^([^:]+):(\\d+):(\\d+): ([RCWEF]): (\\S+): (.*)$";

    /**
     * Creates a new instance of {@link RuboCopParser}.
     */
    public RuboCopParser() {
        super(Messages._Warnings_RuboCop_ParserName(),
                Messages._Warnings_RuboCop_LinkName(),
                Messages._Warnings_RuboCop_TrendName(),
                RUBOCOP_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String message = matcher.group(6);
        String category = classifyIfEmpty(matcher.group(5), message);
        String severity = matcher.group(4);

        Priority priority = Priority.NORMAL;
        if ("E".equals(severity) || "F".equals(severity)) {
            priority = Priority.HIGH;
        }

        Warning warning = createWarning(matcher.group(1), getLineNumber(matcher.group(2)), category, message, priority);
        warning.setColumnPosition(getLineNumber(matcher.group(3)));
        return warning;
    }

    @Override
    public String getSmallImage() {
        return RUBOCOP_SMALL_ICON;
    }

    @Override
    public String getLargeImage() {
        return RUBOCOP_LARGE_ICON;
    }
}

