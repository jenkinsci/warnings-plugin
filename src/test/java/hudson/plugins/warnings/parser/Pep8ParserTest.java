package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for the Pep8 Compiler warning.
 *
 * @author Ulli Hafner
 */
public class Pep8ParserTest extends ParserTester {
    private static final String WARNING_TYPE = "Pep8";

    /**
     * Testet ein Test File mit einer Simplen und einer Komplexen Warning.
     *
     * @throws IOException
     *             falls das File nicht geoeffnet werden kann
     */
    @Test
    public void testParseSimpleAndComplexMessage() throws  IOException {
        Pep8Parser parser = new Pep8Parser();

        Collection<FileAnnotation> warnings = parser.parse(openFile());
        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation warning = iterator.next();

        checkWarning(warning, 1, "trailing whitespace", "trunk/src/python/file.py",
                    WARNING_TYPE, "W291", Priority.NORMAL);

        warning = iterator.next();

        checkWarning(warning, 98, "Message #has! 12special-_ chars|?.",
                    "trunk/src/python/file.py", WARNING_TYPE, "E111", Priority.NORMAL);
    }


    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return "pep8Test.txt";
    }
}
