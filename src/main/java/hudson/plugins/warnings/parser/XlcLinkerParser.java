package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for IBM xlC compiler warnings.
 *
 * @author Andrew Gvozdev
 */
@Extension
public class XlcLinkerParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 211259620936831096L;

    /**
     * Creates a new instance of {@link XlcLinkerParser}.
     * Note that the name matches {@link XlcCompilerParser} to unite them as one parser in UI.
     */
    public XlcLinkerParser() {
        super(Messages._Warnings_Xlc_ParserName(),
                Messages._Warnings_Xlc_LinkName(),
                Messages._Warnings_Xlc_TrendName());
    }

    @Override
    protected String getId() {
        return "IBM XLC Linker";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.XlcLinkerParser();
    }
}

