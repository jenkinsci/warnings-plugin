package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.junit.Test;

import static org.junit.Assert.*;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link RuboCopParser}.
 */
public class RuboCopParserTest extends ParserTester {
    private static final String WARNING_TYPE = Messages._Warnings_RuboCop_ParserName().toString(Locale.ENGLISH);

    @Test
    public void testParse() throws IOException {
        Collection<FileAnnotation> warnings = parse(getWarningsFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 2, warnings.size());

        Iterator<Warning> expectedWarnings = Arrays.asList(
                addCol(new Warning("config.ru", 1, WARNING_TYPE, "Style/FrozenStringLiteralComment", "Missing magic comment # frozen_string_literal: true."), 1),
                addCol(new Warning("spec/rails_helper.rb", 6, WARNING_TYPE, "Style/StringLiterals", "Prefer single-quoted strings when you don't need string interpolation or special symbols.", Priority.HIGH), 7)
        ).iterator();

        Iterator<FileAnnotation> iterator = warnings.iterator();
        while (iterator.hasNext()) {
            assertTrue(WRONG_NUMBER_OF_WARNINGS_DETECTED, expectedWarnings.hasNext());
            Warning expectedWarning = expectedWarnings.next();
            checkWarning(iterator.next(), expectedWarning.getPrimaryLineNumber(), expectedWarning.getColumnStart(), expectedWarning.getMessage(), expectedWarning.getFileName(), expectedWarning.getType(), expectedWarning.getCategory(), expectedWarning.getPriority());
        }
    }

    @Override
    protected String getWarningsFile() {
        return "rubocop.log";
    }

    private Collection<FileAnnotation> parse(final String fileName) throws IOException {
        return new RuboCopParser().parse(openFile(fileName));
    }

    private Warning addCol(Warning warning, int col) {
        warning.setColumnPosition(col);
        return warning;
    }
}
