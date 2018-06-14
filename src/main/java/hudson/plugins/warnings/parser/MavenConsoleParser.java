package hudson.plugins.warnings.parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;

import hudson.Extension;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for maven console warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class MavenConsoleParser extends RegexpLineParser {
    private static final String CONSOLE = "";
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";
    private static final int MAX_MESSAGE_LENGTH = 4000;

    private static final long serialVersionUID = 1737791073711198075L;

    /**
     * Pattern for identifying warning or error maven logs.
     * <pre>
     * Pattern:
     * (.*\s\s|)           -> Capture group 1 matches either empty string (e.g. [WARNING] some log) or some text followed by exactly two
     *                        spaces (e.g. 22:07:27  [WARNING] some log)
     * \[(WARNING|ERROR)\] -> Capture group 2 matches either [WARNING] or [ERROR]
     * \s*                 -> matches zero or more spaces
     * (.*)                -> Capture group 3 matches zero or more characters except line breaks, represents the actual error message
     * </pre>
     * <p>
     * Typical maven logs:
     * 1) 22:07:27  [WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
     * 2) [ERROR] The POM for org.codehaus.groovy.maven:gmaven-plugin:jar:1.1 is missing
     */
    private static final String PATTERN = "^(.*\\s\\s|)\\[(WARNING|ERROR)\\]\\s*(.*)$";

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(Messages._Warnings_Maven_ParserName(),
                Messages._Warnings_Maven_LinkName(),
                Messages._Warnings_Maven_TrendName(),
                PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(WARNING) || line.contains(ERROR);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        String category;
        String errorOrWarningGroup = matcher.group(2);
        String errorOrWarningMessage = matcher.group(3);

        if (ERROR.equals(errorOrWarningGroup)) {
            priority = Priority.HIGH;
            category = "Error";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return createWarning(CONSOLE, getCurrentLine(), category, errorOrWarningMessage, priority);
    }

    // FIXME: post processing is quite slow for large number of warnings, see JENKINS-25278
    @Override
    protected Collection<FileAnnotation> postProcessWarnings(final List<FileAnnotation> warnings) {
        LinkedList<FileAnnotation> condensed = new LinkedList<FileAnnotation>();
        int line = -1;
        for (FileAnnotation warning : warnings) {
            if (warning.getPrimaryLineNumber() == line + 1 && !condensed.isEmpty()) {
                FileAnnotation previous = condensed.getLast();
                if (previous.getPriority() == warning.getPriority()) {
                    condensed.removeLast();
                    if (previous.getMessage().length() + warning.getMessage().length() >= MAX_MESSAGE_LENGTH) {
                        condensed.add(new Warning(previous, warning.getPrimaryLineNumber()));
                    }
                    else {
                        condensed.add(new Warning(previous, previous.getMessage() + "\n" + warning.getMessage(), warning.getPrimaryLineNumber()));
                    }
                }
                else {
                    condensed.add(warning);
                }
            }
            else {
                condensed.add(warning);
            }
            line = warning.getPrimaryLineNumber();
        }
        List<FileAnnotation> noBlank = Lists.newArrayList();
        for (FileAnnotation warning : condensed) {
            if (StringUtils.isNotBlank(warning.getMessage())) {
                noBlank.add(warning);
            }
        }
        return noBlank;
    }
}

