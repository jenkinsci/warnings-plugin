package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for gcc 4.x linker warnings.
 *
 * @author Frederic Chateau
 */
@Extension
public class Gcc4LinkerParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -2792019431810134790L;

    /**
     * Creates a new instance of <code>Gcc4LinkerParser</code>.
     */
    public Gcc4LinkerParser() {
        super(Messages._Warnings_gcc4_ParserName(),
                Messages._Warnings_gcc4_LinkName(),
                Messages._Warnings_gcc4_TrendName());
    }

    @Override
    protected String getId() {
        return "GNU compiler 4 (ld)";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.Gcc4LinkerParser();
    }
}

