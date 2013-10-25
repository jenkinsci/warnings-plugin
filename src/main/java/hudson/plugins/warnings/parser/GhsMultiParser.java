package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the GHS Multi compiler warnings.
 *
 * @author Joseph Boulos
 */
@Extension
public class GhsMultiParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 8149238560432255036L;

    /**
     * Creates a new instance of {@link GhsMultiParser}.
     */
    public GhsMultiParser() {
        super(Messages._Warnings_ghs_ParserName(),
                Messages._Warnings_ghs_LinkName(),
                Messages._Warnings_ghs_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.GhsMultiParser();
    }
}

