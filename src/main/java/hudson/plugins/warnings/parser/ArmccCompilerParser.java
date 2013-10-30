package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for armcc compiler warnings.
 *
 * @author Emanuele Zattin
 */
@Extension
public class ArmccCompilerParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -2677728927938443703L;

    /**
     * Creates a new instance of {@link ArmccCompilerParser}.
     */
    public ArmccCompilerParser() {
        super(Messages._Warnings_Armcc_ParserName(),
                Messages._Warnings_Armcc_LinkName(),
                Messages._Warnings_Armcc_TrendName());
    }

    @Override
    protected String getId() {
        return "Armcc";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.ArmccCompilerParser();
    }
}

