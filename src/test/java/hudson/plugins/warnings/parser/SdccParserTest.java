package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

/**
 * Tests the class {@link SdccParser}.
 *
 * @author Kay van der Zander
 */
public class SdccParserTest extends ParserTester {
    private static final String TYPE = new SdccParser().getGroup();

    /**
     * Parses a file with warnings/errors in all styles. it check the amount of error/warnings found
     *
     * @throws IOException 
     */
    @Test
    public void Sdcc_error_size() throws IOException {
        Collection<FileAnnotation> warnings = new SdccParser().parse(openFile("sdcc_Output.txt"));

        assertEquals(WRONG_NUMBER_OF_WARNINGS_DETECTED, 5, warnings.size());
    }

    /**
     * Parses a file and check warning number 1
     * ..\..\..\Lib\Src\AlBus\AlBus.c:806: warning 116: left shifting more than size of object changed to zero
     *
     * @throws IOException
     */
    @Test
    public void Sdcc_warning1() throws IOException {
        FileAnnotation annotation = getErrorNumber(1);
       checkWarning(annotation, 806, "left shifting more than size of object changed to zero",
                "..\\..\\..\\Lib\\Src\\AlBus\\AlBus.c", "warning", Priority.NORMAL);
    }

    /**
     * Parses a file and check warning number 2
     * ..\..\Src\HrCommunication\HrAlTagList.c:125:216: warning: backslash-newline at end of file
     *
     * @throws IOException
     */
   @Test
    public void Sdcc_warning2() throws IOException {
        FileAnnotation annotation = getErrorNumber(2);
       checkWarning(annotation, 125, "backslash-newline at end of file",
                "..\\..\\Src\\HrCommunication\\HrAlTagList.c", "warning", Priority.NORMAL);
    }

    /**
     * Parses a file and check warning number 3
     * ..\..\Src\TriacModulation\TriacModulation.c:144:6: warning: #warning "There is a rounding error in the denominator for ST_Cycles_T to SystemTimer period conversion!"
     *
     * @throws IOException
     */
   @Test
    public void Sdcc_warning3() throws IOException {
        FileAnnotation annotation = getErrorNumber(3);
       checkWarning(annotation, 144, " #warning \"There is a rounding error in the denominator for ST_Cycles_T to SystemTimer period conversion!\"",
                "..\\..\\Src\\TriacModulation\\TriacModulation.c", "warning", Priority.NORMAL);
    }
    
    /**
     * Parses a file and check warning number 4
     * ..\..\Src\TriacModulation\TriacModulation.c:150:6: warning: #warning "There is a rounding error in the denominator for AcPeriod_T to SystemTimer period conversion!"
     *
     * @throws IOException
     */
   @Test
    public void Sdcc_warning4() throws IOException {
        FileAnnotation annotation = getErrorNumber(3);
       checkWarning(annotation, 150, " #warning \"There is a rounding error in the denominator for AcPeriod_T to SystemTimer period conversion!\"",
                "..\\..\\Src\\TriacModulation\\TriacModulation.c", "warning", Priority.NORMAL);
    }

    /**
     * Parses a file and check error number 1
     * ..\..\Src\main.c:27: syntax error: token -> 'Modbus_UpdateTimers' ; column 21
     *
     * @throws IOException
     */
   @Test
    public void Sdcc_error1() throws IOException {
        FileAnnotation annotation = getErrorNumber(3);
       checkWarning(annotation, 27, " token -> 'Modbus_UpdateTimers' ; column 21",
                "..\\..\\Src\\main.c", "syntax error", Priority.HIGH);
    }

    @Override
    protected String getWarningsFile() {
        return "sdcc-nowrap.log";
    }

    protected FileAnnotation getErrorNumber( final int number) throws IOException {
        Collection<FileAnnotation> warnings = new SdccParser().parse(openFile("sdcc_Output.txt"));
        Iterator<FileAnnotation> iterator = warnings.iterator();
        FileAnnotation annotation = iterator.next();

        for(int i = 1; i < number; i++)
        {
            annotation = iterator.next();
        }

        return annotation;
    }
    
}