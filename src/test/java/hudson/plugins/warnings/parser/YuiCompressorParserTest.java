package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link YuiCompressorParser}.
 *
 * @author Emidio Stani
 */
public class YuiCompressorParserTest extends ParserTester {
    private static final String TYPE = new YuiCompressorParser().getGroup();

    /**
     * Parses a file with 3 warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void parseDeprecation() throws IOException {
        Collection<FileAnnotation> warnings = new YuiCompressorParser().parse(openFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 3, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();

        checkWarning(annotation,
                0,
                "Try to use a single 'var' statement per scope."
                + " [match){returnstringResult;}for( ---> var  <--- i=0;match&&i<match]",
                "unknown.file",
                TYPE, "Use single 'var' per scope", Priority.LOW);

        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "The variable replacement has already been declared in the same scope..."
                + " [replace(variable,replacement);}var  ---> replacement <--- =globalStoredVars[name];if(replacement!=]",
                "unknown.file",
               TYPE, "Duplicate variable", Priority.HIGH);

        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Using 'eval' is not recommended. Moreover, using 'eval' reduces the level of compression!"
                + " [function(condition,label){if( ---> eval <--- (condition)){this.doGotolabel(label]",
                "unknown.file",
                TYPE, "Use eval", Priority.HIGH);

    }

    @Override
    protected String getWarningsFile() {
        return "yui.txt";
    }
}
