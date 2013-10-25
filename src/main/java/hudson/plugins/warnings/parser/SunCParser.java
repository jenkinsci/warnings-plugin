package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the SUN Studio C++ compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class SunCParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -1251248150596418456L;

    /**
     * Creates a new instance of <code>HpiCompileParser</code>.
     */
    public SunCParser() {
        super(Messages._Warnings_sunc_ParserName(),
                Messages._Warnings_sunc_LinkName(),
                Messages._Warnings_sunc_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.SunCParser();
    }
}

