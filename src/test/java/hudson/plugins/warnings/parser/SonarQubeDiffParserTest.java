package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;

/**
 * Tests the class {@link SonarQubeIssuesParser}.
 */
public class SonarQubeDiffParserTest extends ParserTester {

    private static final String FILENAME_API = "sonarqube-differential.json";
    private static final String FILENAME_API_MULTIMODULE = "sonarqube-differential-multimodule.json";

    /** {@inheritDoc} */
    @Override
    protected String getWarningsFile() {
        return FILENAME_API;
    }

    /**
     * Parses a differential scan report.
     * @throws IOException
     */
    @Test
    public void reportDifferentialTest () throws IOException {
        Collection<FileAnnotation> warnings = parseFile(getWarningsFile());

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 6, warnings.size());
    }

    /**
     * Parses a differential scan report. The project contains multiple subprojects.
     * @throws IOException
     */
    @Test
    public void reportDifferentialMultimoduleTest () throws IOException {
        Collection<FileAnnotation> warnings = parseFile(FILENAME_API_MULTIMODULE);

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 8, warnings.size());
    }

    private Collection<FileAnnotation> parseFile (final String name) throws IOException {
        return new SonarQubeDiffParser().parse(openFile(name));
    }

}

