package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the YUI Compressor warnings.
 */
@Extension
public class YuiCompressorParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -4807932429496693096L;

    /**
     * Creates a new instance of <code>YuiCompressorParser</code>.
     */
    public YuiCompressorParser() {
        super(Messages._Warnings_YUICompressor_ParserName(),
                Messages._Warnings_YUICompressor_LinkName(),
                Messages._Warnings_YUICompressor_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.YuiCompressorParser();
    }
}
