package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Inter C compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
@Extension
public class IntelCParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 8409744276858003050L;

    /**
     * Creates a new instance of {@link IntelCParser}.
     */
    public IntelCParser() {
        super(Messages._Warnings_IntelC_ParserName(),
                Messages._Warnings_IntelC_LinkName(),
                Messages._Warnings_IntelC_TrendName());
    }

    @Override
    protected String getId() {
        return "Intel compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.IntelCParser();
    }
}


