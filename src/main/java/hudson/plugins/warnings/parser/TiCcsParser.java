package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Texas Instruments Code Composer Studio compiler warnings.
 *
 * @author Jan Linnenkohl
 */
@Extension
public class TiCcsParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -8253481365175984661L;

    /**
     * Creates a new instance of {@link TiCcsParser}.
     */
    public TiCcsParser() {
        super(Messages._Warnings_TexasI_ParserName(),
                Messages._Warnings_TexasI_LinkName(),
                Messages._Warnings_TexasI_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.TiCcsParser();
    }
}

