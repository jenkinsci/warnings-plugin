package com.ullihafner.warningsparser;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;

/**
 * Null object pattern.
 *
 * @author Ulli Hafner
 */
public class NullWarnigsParser implements WarningsParser {

    public Collection<Warning> parse(final Reader reader) throws IOException,
            com.ullihafner.warningsparser.ParsingCanceledException {
        return Collections.emptyList();
    }
}
