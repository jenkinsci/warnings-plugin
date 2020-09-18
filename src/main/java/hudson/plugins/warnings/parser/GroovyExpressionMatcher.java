package hudson.plugins.warnings.parser;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import org.codehaus.groovy.control.CompilationFailedException;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import hudson.plugins.warnings.WarningsDescriptor;

/**
 * Creates a warning based on a regular expression match and groovy script.
 *
 * This class does not use any sandboxing mechanisms to parse or run the Grooy
 * script. Instead, only users with Overall/Run Scripts permission are able to
 * configure parsers that use custom Groovy scripts.
 *
 * @author Ullrich Hafner
 * @deprecated use the new analysis-model library
 */
@Deprecated
public class GroovyExpressionMatcher implements Serializable {
    private static final long serialVersionUID = -2218299240520838315L;

    private final Warning falsePositive;
    private final String script;

    private transient Script compiled;

    /**
     * Creates a new instance of {@link GroovyExpressionMatcher}.
     *
     * @param script
     *            Groovy script
     * @param falsePositive
     *            indicates a false positive
     */
    public GroovyExpressionMatcher(final String script, final Warning falsePositive) {
        this.script = script;
        this.falsePositive = falsePositive;
    }

    private void compileScriptIfNotYetDone() {
        synchronized (script) {
             if (compiled == null) {
                 try {
                     compiled = compile();
                 }
                 catch (CompilationFailedException exception) {
                     LOGGER.log(Level.SEVERE, "Groovy dynamic warnings parser: exception during compiling: ", exception);
                 }
            }
        }
    }

    /**
     * Compiles the script.
     *
     * @return the compiled script
     * @throws CompilationFailedException if the script contains compile errors
     */
    public Script compile() throws CompilationFailedException {
        Binding binding = new Binding();
        binding.setVariable("falsePositive", falsePositive);
        GroovyShell shell = new GroovyShell(WarningsDescriptor.class.getClassLoader(), binding);
        return shell.parse(script);
    }

    /**
     * Creates a new annotation for the specified match.
     *
     * @param matcher
     *            the regular expression matcher
     * @param lineNumber
     *            the current line number
     * @return a new annotation for the specified pattern
     */
    public Warning createWarning(final Matcher matcher, final int lineNumber) {
        try {
            Object result = run(matcher, lineNumber);
            if (result instanceof Warning) {
                return (Warning)result;
            }
        }
        catch (Exception exception) { // NOPMD NOCHECKSTYLE: catch all exceptions of the Groovy script
            LOGGER.log(Level.SEVERE, "Groovy dynamic warnings parser: exception during execution: ", exception);
        }
        return falsePositive;
    }

    /**
     * Runs the groovy script. No exceptions are caught.
     *
     * @param matcher
     *            the regular expression matcher
     * @param lineNumber
     *            the current line number
     * @return unchecked result of the script
     */
    public Object run(final Matcher matcher, final int lineNumber) {
        compileScriptIfNotYetDone();

        Binding binding = compiled.getBinding();
        binding.setVariable("matcher", matcher);
        binding.setVariable("lineNumber", lineNumber);

        try {
            return compiled.run();
        }
        catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Groovy dynamic warnings parser: exception during execution: ", exception);
            return falsePositive;
        }
    }

    /**
     * Creates a new annotation for the specified match.
     *
     * @param matcher
     *            the regular expression matcher
     * @return a new annotation for the specified pattern
     */
    public Warning createWarning(final Matcher matcher) {
        return createWarning(matcher, 0);
    }

    private static final Logger LOGGER = Logger.getLogger(GroovyExpressionMatcher.class.getName());
}

