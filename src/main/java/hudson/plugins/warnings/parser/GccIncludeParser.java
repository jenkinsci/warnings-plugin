package hudson.plugins.warnings.parser;

import hudson.plugins.warnings.util.model.Priority;

import java.util.regex.Matcher;

/**
 * A parser for the gcc compiler -include - warnings.
 */
public class GccIncludeParser extends RegexpLineParser {
    /** Warning type of this parser. */
    static final String WARNING_TYPE = "gcc-include";
    /** Pattern of gcc compiler - include - warnings. */

    /**
     * 1. Checks only for warning
     */
    private static final String GCC_INCLUDE_WARNING_PATTERN = "^(?:                ) from (.+):([0-9]+):$";

    /**
     * 2. Checks for warnings and information
     *

     private static final String GCC_INCLUDE_WARNING_PATTERN = "^(?:In file included|                ) from (.+):([0-9]+)(?:(:)|(,))?";

     *
     */

    /**
     * Creates a new instance of <code>GccIncludeParser</code>.
     */
    public GccIncludeParser() {
        super(GCC_INCLUDE_WARNING_PATTERN, "GNU compiler (gcc-include)");
    }

    /** {@inheritDoc} */
    @Override
    protected Warning createWarning(final Matcher matcher) {
        Priority priority;
        String category;

    	/**
    	 * 1. Checks only for warning
    	 */

    	priority = Priority.NORMAL;
    	category = "GCC INCLUDE warning";



    	/**
    	 * 2. Checks for warnings and information
         *

    	if (":".equalsIgnoreCase(matcher.group(3))) {
    	    priority = Priority.NORMAL;
    	    category = "GCC INCLUDE warning";
            }
            else{
                priority = Priority.LOW;
    	    category = "GCC INCLUDE information";
            }

    	 *
    	 */
        return new Warning(matcher.group(1), getLineNumber(matcher.group(2)), WARNING_TYPE,
                category, "One of the files included there generated a GCC warning or error - See console log for more information", priority);
    }
}
