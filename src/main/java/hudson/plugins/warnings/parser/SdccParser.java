package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;
import org.apache.commons.lang.StringUtils;
import hudson.Extension;
import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for the sdcc C compiler warnings.
 *
 * Please note that SDCC doesn't has uniform error messages.
 * I ignore the "Warning: Non-connected liverange found and extended to connected component of the CFG:iTemp0. Please contact sdcc authors with source code to reproduce"
 * warnings and the linker warning "?ASlink-Warning-Undefined Global '<name>' referenced by module '<name>'" for now.
 * i ignore them because they don't have the right information needed.
 *
 * @author Kay van der Zander
 */
@Extension
public class SdccParser extends RegexpLineParser {
    private static final long serialVersionUID = 7695540852439013422L;
    
    // search for: ..\..\Src\main.c:27: syntax error: token -> 'Modbus_UpdateTimers' ; column 21.
    // search for: ..\..\..\Lib\Src\AlBus\AlBus.c:806: warning 116: left shifting more than size of object changed to zero
    private static final String WARNING_PATTERN = 
    "(.*\\.c).(\\d+).*:\\s(warning|syntax error).*:(.*)";
    /**
     * Creates a new instance of {@link SdccParser}.
     */
    public SdccParser() {
        super(Messages._Warnings_sdcc_ParserName(),
                Messages._Warnings_sdcc_LinkName(),
                Messages._Warnings_sdcc_TrendName(),
                WARNING_PATTERN);
    }

    @Override
    protected String getId() {
        return "SDCC C compiler";
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(3), matcher.group(4), determinePriority(matcher.group(3)));
    }
          
    private Priority determinePriority(final String message) {
        if (message.toLowerCase().contains("error")) {
            return Priority.HIGH;
        } else if (message.toLowerCase().contains("warning")) {
            return Priority.NORMAL;
        } else {
            return Priority.LOW;
        }
    }
}
