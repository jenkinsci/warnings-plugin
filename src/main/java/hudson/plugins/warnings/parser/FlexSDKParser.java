package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Flex SDK compiler warnings.
 *
 * @author Vivien Tintillier
 */
@Extension
public class FlexSDKParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -185055018399324311L;

    /**
     * Creates a new instance of {@link FlexSDKParser}.
     */
    public FlexSDKParser() {
        super(Messages._Warnings_Flex_ParserName(),
                Messages._Warnings_Flex_LinkName(),
                Messages._Warnings_Flex_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.FlexSDKParser();
    }
}

