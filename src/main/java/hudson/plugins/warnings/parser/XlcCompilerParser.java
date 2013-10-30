package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for IBM xlC compiler warnings.
 *
 * @author Andrew Gvozdev
 */
@Extension
public class XlcCompilerParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 5490211629355204910L;

    /**
     * Creates a new instance of {@link XlcCompilerParser}.
     */
    public XlcCompilerParser() {
        super(Messages._Warnings_Xlc_ParserName(),
                Messages._Warnings_Xlc_LinkName(),
                Messages._Warnings_Xlc_TrendName());
    }

    @Override
    protected String getId() {
        return "IBM XLC Compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.XlcCompilerParser();
    }
}

