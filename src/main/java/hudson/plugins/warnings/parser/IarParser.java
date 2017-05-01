package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;
import java.util.*;

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
    private static int GROUP_NUMBER = 3;
    
    // search for: Fatal Error[Pe1696]: cannot open source file "c:\filename.c"
    // search for: c:\filename.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
    private static final String IAR_WARNING_PATTERN = 
        "^(?:\\[.*\\]\\s*)?\\\"?(.*?)\\\"?(?:,|\\()(\\d+)(?:\\s*|\\)\\s*:\\s*)([Ee]rror|Remark|Warning|Fatal [Ee]rror)\\[(\\w+)\\]: (.*)|(.*?)([Ee]rror|Remark|Warning|Fatal [Ee]rror)\\[(\\w+)\\]: (.*)$";
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
        
        if(matcher.group(GROUP_NUMBER) == null) {
            GROUP_NUMBER = 7;
        }
        
        if(isFalsePositive(matcher.group(GROUP_NUMBER))) {
            return FALSE_POSITIVE;
        }
           
        priority = determinePriority(matcher.group(GROUP_NUMBER));
        return composeWarning(matcher, priority);
    }
           
    private Warning composeWarning(final Matcher matcher, final Priority priority) {
        // report for: Fatal Error[Pe1696]: cannot open source file "c:\filename.c"
        String message = matcher.group(5);
        String small_message = matcher.group(9);
        
        if( message == null && small_message != null ) {
            // createWarning( filename, line number, error number (Pe177), message, priority )
            // General error , there is no filename. only a compiler error. 
            return createWarning("", 0, matcher.group(8), small_message, priority);
        }
        // report for: c:\name.h(17) : Fatal Error[Pe1696]: cannot open source file "System/ProcDef_LPC17xx.h"
        // createWarning( filename, line number, error number (Pe177), message, priority )
        return createWarning(matcher.group(1), getLineNumber(matcher.group(2)), matcher.group(4), message, priority);
    }
      
    private Boolean isFalsePositive(final String message) {
        if ("Remark".equals(message)) {
            return Boolean.FALSE;
        } else if ("Warning".equals(message)) {
            return Boolean.FALSE;
        } else if ("rror".equals(message)) {
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
    
    private Priority determinePriority(final String message) {
        // for "Fatal error", "Fatal Error", "Error" and "error"
        if ("rror".equals(message)) {
            return Priority.HIGH;
        } else if ("Warning".equals(message)) {
            return Priority.NORMAL;
        } else {
            return Priority.LOW;
        }
    }
}
