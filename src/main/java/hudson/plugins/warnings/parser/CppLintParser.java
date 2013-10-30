package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for C++ Lint compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class CppLintParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 1737791073711198075L;

    /**
     * Creates a new instance of {@link CppLintParser}.
     */
    public CppLintParser() {
        super(Messages._Warnings_CppLint_ParserName(),
                Messages._Warnings_CppLint_LinkName(),
                Messages._Warnings_CppLint_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.CppLintParser();
    }
}

