package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * Parses a StyleCop (http://code.msdn.microsoft.com/sourceanalysis/) xml report file.
 *
 * @author Sebastian Seidl
 */
@Extension
public class StyleCopParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of {@link StyleCopParser}.
     */
    public StyleCopParser() {
        super(Messages._Warnings_StyleCop_ParserName(),
                Messages._Warnings_StyleCop_LinkName(),
                Messages._Warnings_StyleCop_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.StyleCopParser();
    }
}
