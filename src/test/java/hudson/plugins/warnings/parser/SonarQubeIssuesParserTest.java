package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;

/**
 * Tests the class {@link SonarQubeIssuesParser}.
 */
public class SonarQubeIssuesParserTest extends ParserTester {

    private static final String FILENAME_API = "sonarqube-api.json";
    private static final String FILENAME_API_MULTIMODULE = "sonarqube-api-multimodule.json";

    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return FILENAME_API;
    }

    /**
     * Parses a report taken from the sonarqube issues API.
     * @throws IOException
     */
    @Test
    public void reportApiTest () throws IOException {
        Collection<FileAnnotation> warnings = parseFile(getWarningsFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 32, warnings.size());

        Iterator<FileAnnotation> i = warnings.iterator();
        FileAnnotation warning = i.next();
        assertEquals(warning.getFileName(), "src/com/tsystems/sbs/jenkinslib/SbsBuild.groovy");
        assertEquals(warning.getPrimaryLineNumber(), 631);
    }

    /**
     * Parses a report taken from the sonarqube issues API. The project contains multiple subprojects.
     * @throws IOException
     */
    @Test
    public void reportApiMultimoduleTest () throws IOException {
        Collection<FileAnnotation> warnings = parseFile(FILENAME_API_MULTIMODULE);

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 106, warnings.size());

        Iterator<FileAnnotation> i = warnings.iterator();
        FileAnnotation warning = i.next();
        assertEquals(warning.getFileName(), "cart-common-folder/src/main/java/com/example/sonarqube/CloseResource.java");
        assertEquals(warning.getPrimaryLineNumber(), -1);
    }

    private Collection<FileAnnotation> parseFile (final String name) throws IOException {
        return new SonarQubeIssuesParser().parse(openFile(name));
    }

}

