package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.Extension;

import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for the IAR C/C++ compiler warnings. Note, that since release 4.1
 * this parser requires that IAR compilers are started with option
 * '----no_wrap_diagnostics'. Then the IAR compilers will create single-line
 * warnings.
 *
 * @author Claus Klein
 * @author Ulli Hafner
 * @author Kay van der Zander
 */
@Extension
public class IarParser extends RegexpLineParser {
    private static final long serialVersionUID = 7695540852439013425L;
    private static final int GROUP_NUMBER = 2

    // search for: Fatal Error[Pe1696]: cannot open source file "c:\JenkinsJobs\900ZH\Workspace\Platform.900\Src\Safety\AirPressureSwitch.c"
    // search for: c:\JenkinsJobs\900ZH\Workspace\Product.900ZH\Src\System\AdditionalResources.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
    private static final String IAR_WARNING_PATTERN = 
        "(.*?)(Error|Remark|Warning|Fatal Error|Fatal error)\\[(\\w+)\\]:(.*)$";
    //     G1                         G2                         G3       G4
    /**
     * Creates a new instance of {@link IarParser}.
     */
    public IarParser() {
        super(Messages._Warnings_iar_ParserName(),
                Messages._Warnings_iar_LinkName(),
                Messages._Warnings_iar_TrendName(),
                IAR_WARNING_PATTERN, true);
    }

    @Override
    protected boolean isLineInteresting(final String line) {
        return line.contains("Warning") || line.contains("rror") || line.contains("Remark");
    }

    @Override
    protected String getId() {
        return "IAR compiler (C/C++)";
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        if ("Remark".equals(matcher.group(GROUP_NUMBER))) {
            priority = Priority.LOW;
        }
        else if ("Warning".equals(matcher.group(GROUP_NUMBER))) {
            priority = Priority.NORMAL;
        }
        // for "Fatal error", "Fatal Error", "Error" and "error"
        else if ("rror".equals(matcher.group(GROUP_NUMBER))) {
            priority = Priority.HIGH;
        }
        else if ("Fatal".equals(matcher.group(1))) {
            priority = Priority.HIGH;
        }
        else {
            return FALSE_POSITIVE;
        }
        
        // report for: Fatal Error[Pe1696]: cannot open source file "c:\JenkinsJobs\900ZH\Workspace\Platform.900\Src\Safety\AirPressureSwitch.c"
        if (isSmallPattern(matcher.group(1))) {
            String message = normalizeWhitespaceInMessage(matcher.group(4));
            String[] parts = message.split('"');
            // createWarning( filename, line number, error number (Pe177), message, priority )
            return createWarning(parts[1], getLineNumber(0), matcher.group(3), parts[0], priority);
        }

        // report for: c:\JenkinsJobs\900ZH\Workspace\Product.900ZH\Src\System\AdditionalResources.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
        String message = normalizeWhitespaceInMessage(matcher.group(1));
        String[] parts = message.split("()");
        // createWarning( filename, line number, error number (Pe177), message, priority )
        return createWarning(parts[0], getLineNumber(parts[1]), matcher.group(3), matcher.group(4), priority);
    }
   
    private String normalizeWhitespaceInMessage(final String message) {
        return message.replaceAll("\\s+", " ");
    }
    
    private Boolean isSmallPattern(final String message) {
        if (message = "") {
            return true;
        } else {
           return false;
        }
    }
}
