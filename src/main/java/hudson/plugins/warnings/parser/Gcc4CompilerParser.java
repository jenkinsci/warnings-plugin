package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for gcc 4.x compiler warnings.
 *
 * @author Frederic Chateau
 */
@Extension
public class Gcc4CompilerParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 5490211629355204910L;

    /**
     * Creates a new instance of <code>Gcc4CompilerParser</code>.
     */
    public Gcc4CompilerParser() {
        super(Messages._Warnings_gcc4_ParserName(),
                Messages._Warnings_gcc4_LinkName(),
                Messages._Warnings_gcc4_TrendName());
    }

    @Override
    protected String getId() {
        return "GNU compiler 4 (gcc)";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.Gcc4CompilerParser();
    }
}

