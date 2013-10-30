package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Oracle Invalids.
 *
 * @author Ulli Hafner
 */
@Extension
public class InvalidsParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 440910718005095427L;

    /**
     * Creates a new instance of {@link InvalidsParser}.
     */
    public InvalidsParser() {
        super(Messages._Warnings_OracleInvalids_ParserName(),
                Messages._Warnings_OracleInvalids_LinkName(),
                Messages._Warnings_OracleInvalids_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.InvalidsParser();
    }
}

