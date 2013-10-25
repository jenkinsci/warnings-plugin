package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the tnsdl translator warnings.
 *
 * @author Shaohua Wen
 */
@Extension
public class TnsdlParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -7740789998865369930L;

    /**
     * Creates a new instance of {@link TnsdlParser}.
     */
    public TnsdlParser() {
        super(Messages._Warnings_TNSDL_ParserName(),
                Messages._Warnings_TNSDL_LinkName(),
                Messages._Warnings_TNSDL_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.TnsdlParser();
    }
}

