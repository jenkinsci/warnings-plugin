package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Diab C++ compiler warnings.
 *
 * @author Yuta Namiki
 */
@Extension
public class DiabCParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -1251248150596418456L;

    /**
     * Creates a new instance of <code>HpiCompileParser</code>.
     */
    public DiabCParser() {
        super(Messages._Warnings_diabc_ParserName(),
                Messages._Warnings_diabc_LinkName(),
                Messages._Warnings_diabc_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.DiabCParser();
    }
}

