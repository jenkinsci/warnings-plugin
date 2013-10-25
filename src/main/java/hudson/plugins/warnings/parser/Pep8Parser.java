package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Pep8 compiler warnings.
 *
 * @author Marvin Schütz
 */
@Extension
public class Pep8Parser extends AbstractWarningsParser {
    private static final long serialVersionUID = -8444940209330966997L;

    /**
     * Creates a new instance of {@link Pep8Parser}.
     */
    public Pep8Parser() {
        super(Messages._Warnings_Pep8_ParserName(),
                Messages._Warnings_Pep8_LinkName(),
                Messages._Warnings_Pep8_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.Pep8Parser();
    }
}

