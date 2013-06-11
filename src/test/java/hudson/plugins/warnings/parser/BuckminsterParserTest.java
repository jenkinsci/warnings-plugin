package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link BuckminsterParser}.
 */
public class BuckminsterParserTest extends ParserTester {
    private static final String TYPE = new BuckminsterParser().getGroup();

    /**
     * Parses a file with three Buckminster warnings.
     *
     * @throws IOException
     *      if the file could not be read
     */
    @Test
    public void testWarningsParser() throws IOException {
        Collection<FileAnnotation> warnings = new BuckminsterParser().parse(openFile());

        assertEquals("Wrong number of warnings detected.", 3, warnings.size());

        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();
        checkWarning(annotation,
                43,
                "ArrayList is a raw type. References to generic type ArrayList<E> should be parameterized",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java",
                TYPE, "", Priority.NORMAL);
        annotation = iterator.next();
        checkWarning(annotation,
                57,
                "Type safety: The method toArray(Object[]) belongs to the raw type ArrayList. References to generic type ArrayList<E> should be parameterized",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp/src/org/eclipse/buckminster/tutorial/mailapp/NavigationView.java",
                TYPE, "", Priority.HIGH);
        annotation = iterator.next();
        checkWarning(annotation,
                0,
                "Build path specifies execution environment J2SE-1.5. There are no JREs installed in the workspace that are strictly compatible with this environment.",
                "/var/lib/hudson/jobs/MailApp/workspace/plugins/org.eclipse.buckminster.tutorial.mailapp",
                TYPE, "", Priority.NORMAL);
    }

    @Override
    protected String getWarningsFile() {
        return "buckminster.txt";
    }
}

