package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Buckminster compiler warnings.
 *
 * @author Johannes Utzig
 */
@Extension
public class BuckminsterParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -3723799140297979579L;

    /**
     * Creates a new instance of {@link BuckminsterParser}.
     */
    public BuckminsterParser() {
        super(Messages._Warnings_Buckminster_ParserName(),
                Messages._Warnings_Buckminster_LinkName(),
                Messages._Warnings_Buckminster_TrendName());
    }

    @Override
    protected String getId() {
        return "Buckminster Compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.BuckminsterParser();
    }
}

