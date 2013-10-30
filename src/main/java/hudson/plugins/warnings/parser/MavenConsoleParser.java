package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for maven console warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class MavenConsoleParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 1737791073711198075L;

    /**
     * Creates a new instance of {@link MavenConsoleParser}.
     */
    public MavenConsoleParser() {
        super(Messages._Warnings_Maven_ParserName(),
                Messages._Warnings_Maven_LinkName(),
                Messages._Warnings_Maven_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.MavenConsoleParser();
    }
}

