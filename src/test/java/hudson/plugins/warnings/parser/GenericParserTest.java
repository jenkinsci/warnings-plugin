package hudson.plugins.warnings.parser;

import static junit.framework.Assert.*;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

/**
 * Tests the class {@link GenericParser}.
 *
 * @author Andreas Lüthi
 */
public class GenericParserTest extends ParserTester {
    /** Error message. */
    private static final String WRONG_NUMBER_OF_WARNINGS_DETECTED = "Wrong number of warnings detected.";

    @Test
    public void testWarningsParser() throws IOException {

        Collection<FileAnnotation> warnings = new GenericParser().parse(openFile());
        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 13, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation, 123, "the warning message", "file-name-with-full-path",
                "Warning-Type", "the category", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(
                annotation,
                36,
                "mismatched input 'descn' expecting 'end'",
                "D:/Build/work/hudson-test/jobs/LUA-acf-abcd-BuildIntegrationTest/workspace/ABS_Sources/SG_A3.1/BASE PAR DEF/ASNT$DWIA.BASE PAR DEF",
                "ACS Marker", "org.eclipse.xtext.diagnostics.Diagnostic.Syntax", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(
                annotation,
                19,
                "comment with: more columns ::: ?",
                "D:/Build/work/hudson-test/jobs/LUA-acf-abcd-BuildIntegrationTest/workspace/ABS_Sources/SG_A3.1/TASK TEMPL/TS_POS.0V.TASK TEMPL",
                "ACS Marker 4711",
                "TaskTemplJavaValidator.TASKTEMPLATEEPILOGWITHTASKNAME_TASKTEMPLATEEPILOG",
                Priority.NORMAL);
    }

    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "genericmarker.txt";
    }
}
