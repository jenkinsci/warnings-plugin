package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.Extension;

import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for RTTests Error Messages.
 *
 * @author Benedikt Spranger
 */
@Extension
public class RTTestsParser extends RegexpLineParser {
    private static final long serialVersionUID = 2993328034978892249L;

    /** Pattern of RTTests warnings. */
    private static final String CYCLICTEST_WARNING_PATTERN =
        "^(WARN(ING)?|FATAL):(.*)$";

    /**
     * Creates a new instance of {@link RTTestsParser}.
     */
    public RTTestsParser() {
        super(Messages._Warnings_RTTests_ParserName(),
              Messages._Warnings_RTTests_LinkName(),
              Messages._Warnings_RTTests_TrendName(),
              CYCLICTEST_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String category = matcher.group(1).trim();
        String message = matcher.group(3).trim();
        Priority priority = Priority.NORMAL;

        if (category.equals("FATAL"))
            priority = Priority.HIGH;

        return createWarning("Nil", 0, category, message, priority);
    }
}
