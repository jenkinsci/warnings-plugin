package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Acu Cobol compile.
 *
 * @author jerryshea
 */
@Extension
public class AcuCobolParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -894639209290549425L;

    /**
     * Creates a new instance of {@link AcuCobolParser}.
     */
    public AcuCobolParser() {
        super(Messages._Warnings_AcuCobol_ParserName(),
                Messages._Warnings_AcuCobol_LinkName(),
                Messages._Warnings_AcuCobol_TrendName());
    }

    @Override
    protected String getId() {
        return "AcuCobol Compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.AcuCobolParser();
    }

}

