package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the GNU Make and Gcc4 compiler warnings. Read GNU Make output to
 * know where compilation are run.
 *
 * @author vichak
 */
@Extension
public class GnuMakeGccParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -67701741403245309L;

    /**
     * Creates a new instance of {@link GnuMakeGccParser}.
     */
    public GnuMakeGccParser() {
        super(Messages._Warnings_GnuMakeGcc_ParserName(),
                Messages._Warnings_GnuMakeGcc_LinkName(),
                Messages._Warnings_GnuMakeGcc_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.GnuMakeGccParser();
    }
}
