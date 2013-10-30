package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for Perforce execution.
 *
 * @author Adrian Deccico
 */
public class P4Parser extends RegexpLineParser {
    private static final String ALREADY_OPENED = "already opened for edit";
    private static final String CANT_ADD = "can't add existing file";
    private static final String WARNING_ADD_OF = "warning: add of existing file";
    private static final String OPENED_FOR_EDIT = "can't add \\(" + ALREADY_OPENED + "\\)";
    private static final String NOTHING_CHANGED = "nothing changed";
    private static final String OR = "|";

    /** Pattern of perforce compiler warnings. */
    private static final String PERFORCE_WARNING_PATTERN = "^(.*) - " + "(" + CANT_ADD + OR + WARNING_ADD_OF + OR
            + OPENED_FOR_EDIT + OR + NOTHING_CHANGED + ")" + "(.*)$";

    /**
     * Creates a new instance of {@link P4Parser}.
     */
    public P4Parser() {
        super(PERFORCE_WARNING_PATTERN, true);
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        String category = matcher.group(2).trim();
        String fileName = matcher.group(1).trim();
        String message = fileName;
        Priority p = Priority.NORMAL;
        if (category.contains(ALREADY_OPENED) || category.equals(NOTHING_CHANGED)) {
            p = Priority.LOW;
        }
        return createWarning(fileName, 0, category, message, p);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains(" - ");
    }
}
