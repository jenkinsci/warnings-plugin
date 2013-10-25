package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Doxygen warnings.
 *
 * @author Frederic Chateau
 * @author Bruno Matos
 */
@Extension
public class DoxygenParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -6770174143703245309L;

    /**
     * Creates a new instance of {@link DoxygenParser}.
     */
    public DoxygenParser() {
        super(Messages._Warnings_Doxygen_ParserName(),
                Messages._Warnings_Doxygen_LinkName(),
                Messages._Warnings_Doxygen_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.DoxygenParser();
    }
}
