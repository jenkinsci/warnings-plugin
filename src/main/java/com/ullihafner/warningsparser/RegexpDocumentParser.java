package com.ullihafner.warningsparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import hudson.console.ConsoleNote;

/**
 * Parses an input stream as a whole document for compiler warnings using the provided regular expression.
 *
 * @author Ulli Hafner
 */
public abstract class RegexpDocumentParser extends RegexpParser {

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param warningPattern
     *            pattern of compiler warnings.
     * @param useMultiLine
     *            Enables multi line mode. In multi line mode the expressions <tt>^</tt> and <tt>$</tt> match just after
     *            or just before, respectively, a line terminator or the end of the input sequence. By default these
     *            expressions only match at the beginning and the end of the entire input sequence.
     */
    public RegexpDocumentParser(final String warningPattern, final boolean useMultiLine) {
        super(warningPattern, useMultiLine);
    }

    @Override
    public Collection<Warning> parse(final Reader file) throws IOException, ParsingCanceledException {
        BufferedReader reader = new BufferedReader(file);
        StringBuilder buf = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            buf.append(ConsoleNote.removeNotes(line)).append("\n");
            line = reader.readLine();
        }

        String content = buf.toString();

        file.close();

        ArrayList<Warning> warnings = new ArrayList<Warning>();
        findAnnotations(content, warnings);

        return warnings;
    }
}
