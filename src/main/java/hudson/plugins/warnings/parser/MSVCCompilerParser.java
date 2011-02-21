package hudson.plugins.warnings.parser;

import hudson.plugins.analysis.util.model.Priority;

import java.util.regex.Matcher;

/**
 * Parser for the Microsoft Visual C++ compiler.
 *
 */
public class MSVCCompilerParser extends RegexpLineParser {
    /** A GCC error. */
    static final String WARNING_CATEGORY = "Normal";
    /** Warning type of this parser. */
    static final String WARNING_TYPE = "MSVC";
    /** Pattern of MSVC compiler warnings. */
    private static final String MSVC_WARNING_PATTERN = "(\\S*)\\((\\d*)\\) : warning ([^:]*):(.*)";

    /**
     * Creates a new instance of <code>MSVCCompilerParser</code>.
     */
    public MSVCCompilerParser() {
        super(MSVC_WARNING_PATTERN, "Microsoft Visual C++ compiler");
    }
    /** {@inheritDoc} */
    @Override
    protected Warning createWarning(final Matcher matcher) {
        String fileName = matcher.group(1);
        int lineNumber = getLineNumber(matcher.group(2));
        String message = matcher.group(3) + matcher.group(4);

        Priority priority;

        if (matcher.group(3).equalsIgnoreCase("error")) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }

        return new Warning(fileName, lineNumber, WARNING_TYPE, WARNING_CATEGORY, message, priority);
    }
}

