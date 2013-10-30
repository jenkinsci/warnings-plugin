package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the gcc compiler warnings.
 *
 * @author Greg Roth
 */
@Extension
public class GccParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 2020182274225690532L;

    /**
     * Creates a new instance of {@link GccParser}.
     */
    public GccParser() {
        super(Messages._Warnings_gcc3_ParserName(),
                Messages._Warnings_gcc3_LinkName(),
                Messages._Warnings_gcc3_TrendName());
    }

    @Override
    protected String getId() {
        return "GNU compiler (gcc)";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.GccParser();
    }
}

