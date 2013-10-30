package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Coolflux DSP Compiler warnings.
 *
 * @author Vangelis Livadiotis
 */
@Extension
public class CoolfluxChessccParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 4742509996511002391L;

    /**
     * Creates a new instance of {@link CoolfluxChessccParser}.
     */
    public CoolfluxChessccParser() {
        super(Messages._Warnings_Coolflux_ParserName(),
                Messages._Warnings_Coolflux_LinkName(),
                Messages._Warnings_Coolflux_TrendName());
    }

    @Override
    protected String getId() {
        return "Coolflux DSP Compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.CoolfluxChessccParser();
    }
}


