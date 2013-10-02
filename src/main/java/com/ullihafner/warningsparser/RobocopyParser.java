package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for Robocopy.
 *
 * @author Adrian Deccico
 *
 * captured samples:
 *    *EXTRA File                  0        b           Unknown Task
 *   100%        New File                   0        a.log
 *                 same                 0        a.log
 */
public class RobocopyParser extends RegexpLineParser {
    /** Pattern of perforce compiler warnings. */
    private static final String ROBOCOPY_WARNING_PATTERN = "^(.*)(EXTRA File|New File|same)\\s*(\\d*)\\s*(.*)$";

    /**
     * Creates a new instance of {@link RobocopyParser}.
     */
    public RobocopyParser() {
        super(ROBOCOPY_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String file = matcher.group(4).split("\\s{11}")[0];
        String message = file;
        String category = matcher.group(2);
        return createWarning(file, 0, category, message, Priority.NORMAL);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("        ");
    }
}

