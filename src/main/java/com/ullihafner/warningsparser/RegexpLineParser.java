package com.ullihafner.warningsparser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;

import hudson.console.ConsoleNote;

/**
 * Parses an input stream line by line for compiler warnings using the provided regular expression. Multiple line
 * regular expressions are not supported, each warnings has to be one a single line.
 *
 * @author Ulli Hafner
 */
public abstract class RegexpLineParser extends RegexpParser {

    /**
     * Determines if a line is checked for a string existence before the regular expression is applied.
     *
     * @see #isLineInteresting(String)
     */
    private final boolean isStringMatchActivated;

    private int currentLine;

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param warningPattern
     *            pattern of compiler warnings.
     * @param isStringMatchActivated
     *            determines if a line is checked for a string existence before the regular expression is applied
     */
    public RegexpLineParser(final String warningPattern, final boolean isStringMatchActivated) {
        super(warningPattern, false);

        this.isStringMatchActivated = isStringMatchActivated;
    }

    /**
     * Creates a new instance of {@link RegexpDocumentParser}.
     *
     * @param warningPattern
     *            pattern of compiler warnings.
     */
    public RegexpLineParser(final String warningPattern) {
        this(warningPattern, false);
    }

    @Override
    public Collection<Warning> parse(final Reader file) throws IOException, ParsingCanceledException {
        ArrayList<Warning> warnings = new ArrayList<Warning>();

        LineIterator iterator = IOUtils.lineIterator(file);
        try {
            currentLine = 0;
            if (isStringMatchActivated) {
                while (iterator.hasNext()) {
                    String line = getNextLine(iterator);
                    if (isLineInteresting(line)) {
                        findAnnotations(line, warnings);
                    }
                    currentLine++;
                }
            }
            else {
                while (iterator.hasNext()) {
                    findAnnotations(getNextLine(iterator), warnings);
                    currentLine++;
                }
            }
        }
        finally {
            iterator.close();
        }

        return postProcessWarnings(warnings);
    }

    /**
     * Post processes the warnings. This default implementation does nothing.
     *
     * @param warnings
     *            the warnings after the parsing process
     * @return the post processed warnings
     */
    protected Collection<Warning> postProcessWarnings(final List<Warning> warnings) {
        return warnings;
    }

    /**
     * Returns the number of the current line in the parsed file.
     *
     * @return the current line
     */
    public int getCurrentLine() {
        return currentLine;
    }

    private String getNextLine(final LineIterator iterator) {
        return ConsoleNote.removeNotes(iterator.nextLine());
    }

    /**
     * Returns whether the specified line is interesting. Each interesting line will be handled by the defined regular
     * expression. Here a parser can implement some fast checks (i.e. string or character comparisons) in order to see
     * if a required condition is met.
     *
     * @param line
     *            the line to inspect
     * @return <code>true</code> if the line should be handed over to the regular expression scanner, <code>false</code>
     *         if the line does not contain a warning.
     */
    protected boolean isLineInteresting(final String line) {
        return true;
    }

}
