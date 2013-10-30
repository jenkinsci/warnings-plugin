package com.ullihafner.warningsparser;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for maven console warnings.
 *
 * @author Ulli Hafner
 */
public class MavenConsoleParser extends RegexpLineParser {
    private static final String CONSOLE = "";
    private static final String WARNING = "WARNING";
    private static final String ERROR = "ERROR";

    private static final String PATTERN = "^.*\\[(WARNING|ERROR)\\]\\s*(.*)$";

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(WARNING) || line.contains(ERROR);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        String category;
        if (ERROR.equals(matcher.group(1))) {
            priority = Priority.HIGH;
            category = "Error";
        }
        else {
            priority = Priority.NORMAL;
            category = "Warning";
        }
        return createWarning(CONSOLE, getCurrentLine(), category, matcher.group(2), priority);
    }

    @Override
    protected Collection<Warning> postProcessWarnings(final List<Warning> warnings) {
        List<Warning> condensed = Lists.newArrayList();
        int line = -1;
        for (Warning warning : warnings) {
            if (warning.getPrimaryLineNumber() == line + 1 && !condensed.isEmpty()) {
                Warning previous = condensed.get(condensed.size() - 1);
                if (previous.getPriority() == warning.getPriority()) {
                    condensed.remove(condensed.size() - 1);
                    condensed.add(new Warning(previous, warning.getMessage(), warning.getPrimaryLineNumber()));
                }
            }
            else {
                condensed.add(warning);
            }
            line = warning.getPrimaryLineNumber();
        }
        List<Warning> noBlank = Lists.newArrayList();
        for (Warning warning : condensed) {
            if (StringUtils.isNotBlank(warning.getMessage())) {
                noBlank.add(warning);
            }
        }
        return noBlank;
    }
}
