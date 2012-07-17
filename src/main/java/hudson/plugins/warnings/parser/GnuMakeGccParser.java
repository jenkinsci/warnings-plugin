package hudson.plugins.warnings.parser;

import hudson.Extension;
import hudson.plugins.analysis.util.model.Priority;
import java.util.regex.Matcher;

/**
 * A parser for the GNU Make and Gcc4 compiler warnings. Read GNU Make output to
 * know where compilation are run.
 *
 * @author vichak
 */
@Extension
public class GnuMakeGccParser extends RegexpLineParser {

    private static final long serialVersionUID = -67701741403245309L;
    private static final String ERROR = "error";
    static final String GCC_ERROR = "GCC error";
    static final String LINKER_ERROR = "Linker error";
    private static final String GNUMAKEGCC_WARNING_PATTERN = "^("
            + "(?:.*\\[.*\\])?\\s*" //ANT_TASK
            + "(.*\\.[chpimxsola0-9]+):(\\d+):(?:\\d+:)? (warning|error): (.*)$" //GCC 4 warning
            + ")|("
            + "(^make\\[.*\\]: Entering directory)\\s*(\\`((.*))\\')" //handle make entrering directory
            + ")";
    private String directory;

    public GnuMakeGccParser() {
        super(Messages._Warnings_GnuMakeGcc_ParserName(),
                Messages._Warnings_GnuMakeGcc_LinkName(),
                Messages._Warnings_GnuMakeGcc_TrendName(),
                GNUMAKEGCC_WARNING_PATTERN);
        directory = "";
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        if (matcher.group(1) != null) {
            String fileName = matcher.group(2);
            int lineNumber = getLineNumber(matcher.group(3));
            String message = matcher.group(5);
            Priority priority;
            String category;
            if (ERROR.equalsIgnoreCase(matcher.group(4))) {
                priority = Priority.HIGH;
                category = "Error";
            } else {
                priority = Priority.NORMAL;
                category = "Warning";
            }
			if (fileName.startsWith("/")){
				return createWarning(fileName, lineNumber, category, message, priority);
			} else {
			    return createWarning(directory + fileName, lineNumber, category, message, priority);
			}
        } else {
            directory = matcher.group(9) + "/";
            return FALSE_POSITIVE;
        }
    }
}
