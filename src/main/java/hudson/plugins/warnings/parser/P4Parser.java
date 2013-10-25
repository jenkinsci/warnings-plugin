package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Perforce execution.
 *
 * @author Adrian Deccico
 */
@Extension
public class P4Parser extends AbstractWarningsParser {
    private static final long serialVersionUID = -8106854254745366432L;

    /**
     * Creates a new instance of {@link P4Parser}.
     */
    public P4Parser() {
        super(Messages._Warnings_Perforce_ParserName(),
                Messages._Warnings_Perforce_LinkName(),
                Messages._Warnings_Perforce_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.P4Parser();
    }
}

