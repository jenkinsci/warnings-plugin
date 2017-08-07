package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link RTTestsParser}.
 *
 * @author Benedikt Spranger
 */
public class RTTestsParserTest extends ParserTester {
    private static final String TYPE = new RTTestsParser().getGroup();
    private static final String FILENAME = "cyclictest.log";

    /**
     * Parses a file with three RTTests warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = new RTTestsParser().parse(openFile());

        assertEquals("Wrong number of warnings detected.", 4, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation,
                     0,
                     "Running on unknown kernel version...YMMV",
                     "Nil",
                     TYPE, "WARN", Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                     0,
                     "Running on unknown kernel version...YMMV",
                     "Nil",
                     TYPE, "WARNING", Priority.NORMAL);

        annotation = iterator.next();
        checkWarning(annotation,
                     0,
                     "numa and smp options are mutually exclusive",
                     "Nil",
                     TYPE, "FATAL", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                     0,
                     "could not mount debugfs",
                     "Nil",
                     TYPE, "FATAL", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return FILENAME;
    }
}
