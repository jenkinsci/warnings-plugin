package hudson.plugins.warnings.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link IarParser}.
 *
 * @author Ulli Hafner
 */
public class IarParserTest extends ParserTester {
    private static final String TYPE = new IarParser().getGroup();

    /**
     * Parses a file with warnings/errors in all styles. it check the amount of error/warnings found
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
    @Test
    public void IAR_error_size() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 6, warnings.size());
    }

    /**
     * Parsesa file and check error number 1
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
   @Test
    public void IAR_error1() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        Iterator<FileAnnotation> iterator = warnings.iterator();

        FileAnnotation annotation = iterator.next();
       /*checkWarning(annotation, 3767, "Pe188",
                "D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c",
                "enumerated type mixed with another type", Priority.NORMAL);*/
    }
    
     /**
     * Parses a file and check error number 2
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
  /* @Test
    public void IAR_error2() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        FileAnnotation annotation = warnings.iterator().next();
        checkWarning(annotation, 3767, "enumerated type mixed with another type",
                "D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c",
                "Pe188", Priority.NORMAL);
    }
    
     /**
     * Parses a file and check error number 3
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
  /* @Test
    public void IAR_error3() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        FileAnnotation annotation = warnings.iterator().next().next();
        checkWarning(annotation, 3918, "enumerated type mixed with another type",
                "D:/continuousIntegration/modifiedcomps/forcedproduct/MHSM-Cascade/Cascade-Config/config/src/RDR_Config.c",
                "Pe188", Priority.NORMAL);
    }
    
     /**
     * Parses a file and check error number 4
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
  /* @Test
    public void IAR_error4() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        FileAnnotation annotation = warnings.iterator().next().next().next();
        checkWarning(annotation, 17, "cannot open source file \"System/ProcDef_LPC17xx.h\"",
                "c:/JenkinsJobs/900ZH/Workspace/Product.900ZH/Src/System/AdditionalResources.h",
                "Pe1696", Priority.HIGH);
    }
    
    /**
     * Parses a file and check error number 5
     *
     * @throws IOException
     *      if the file could not be read
     * @see <a href="http://issues.jenkins-ci.org/browse/JENKINS-8823">Issue 8823</a>
     */
   /*@Test
    public void IAR_error5() throws IOException {
        Collection<FileAnnotation> warnings = new IarParser().parse(openFile("issue8823.txt"));

        FileAnnotation annotation = warnings.iterator().next().next().next().next();
        checkWarning(annotation, 43, "variable \"pgMsgEnv\" was declared but never referenced",
                "C:/dev/bsc/daqtask.c",
                "Pe177", Priority.NORMAL);
    }*/

    @Override
    protected String getWarningsFile() {
        return "iar-nowrap.log";
    }
}

