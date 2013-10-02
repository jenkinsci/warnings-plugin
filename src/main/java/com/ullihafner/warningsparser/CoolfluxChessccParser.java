package com.ullihafner.warningsparser;

import java.util.regex.Matcher;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * A parser for the Coolflux DSP Compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
public class CoolfluxChessccParser extends RegexpLineParser {
    private static final String CHESSCC_PATTERN = "^.*?Warning in \"([^\"]+)\", line (\\d+),.*?:\\s*(.*)$";

    /**
     * Creates a new instance of {@link CoolfluxChessccParser}.
     */
    public CoolfluxChessccParser() {
        super(CHESSCC_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning");
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(3), Priority.HIGH);
    }
}
