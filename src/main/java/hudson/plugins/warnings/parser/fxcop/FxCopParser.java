package hudson.plugins.warnings.parser.fxcop;

import hudson.Extension;

import hudson.plugins.warnings.parser.AbstractWarningsParser;
import hudson.plugins.warnings.parser.Messages;

/**
 * Parses a fxcop xml report file. This does not uses the XML Pull parser as it
 * can not handle the FxCop XML files. The bug is registered at Sun as http:
 * //bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
 * <p>
 * Note that instances of this parser are not thread safe.
 * </p>
 */
@SuppressWarnings("unused")
@Extension
public class FxCopParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -7208558002331355408L;

    /**
     * Creates a new instance of {@link FxCopParser}.
     */
    public FxCopParser() {
        super(Messages._Warnings_FxCop_ParserName(),
                Messages._Warnings_FxCop_LinkName(),
                Messages._Warnings_FxCop_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.fxcop.FxCopParser();
    }
}
