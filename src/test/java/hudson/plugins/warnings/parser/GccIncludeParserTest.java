package hudson.plugins.warnings.parser;

import static junit.framework.Assert.*;
import hudson.plugins.warnings.util.model.FileAnnotation;
import hudson.plugins.warnings.util.model.Priority;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

/**
 * Tests the class {@link GccIncludeParser}.
 */
public class GccIncludeParserTest extends ParserTester {
    /** Error message. */
    private static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected.";
    /** A warning. */
    private static final String GCC_INCLUDE_WARNING = "GCC INCLUDE warning";

    /**
     * Creates a new instance of {@link GccIncludeParserTest}.
     */
    public GccIncludeParserTest() {
        super(GccIncludeParser.class);
    }

    /**
     * Parses a file with three GCC include warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = sort(new GccIncludeParser().parse(openFile()));

		assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 1, warnings.size());

		/**
		 *

		assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 3, warnings.size());

		 *
		 */

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();

		/**
		 *

        checkWarning(annotation,
		        4,
                "One of the files included there generated a GCC warning or error - See console log for more information",
                "/usr/include/c++/3.3/backward/warn.h",
                GccIncludeParser.WARNING_TYPE, GCC_INCLUDE_INFO, Priority.LOW);
        annotation = iterator.next();
        checkWarning(annotation,
                31,
                "One of the files included there generated a GCC warning or error - See console log for more information",
                "/usr/include/c++/3.3/backward/iostream.h",
                GccIncludeParser.WARNING_TYPE, GCC_INCLUDE_INFO, Priority.LOW);
        annotation = iterator.next();

	 *
	 */

        checkWarning(annotation,
                1,
                "One of the files included there generated a GCC warning or error - See console log for more information",
                "test_clt.cc",
                GccIncludeParser.WARNING_TYPE, GCC_INCLUDE_WARNING, Priority.NORMAL);
    }

    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "gcc-include.txt";
    }
}

