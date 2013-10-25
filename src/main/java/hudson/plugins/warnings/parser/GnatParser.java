package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Gnat compiler warnings.
 *
 * @author Bernhard Berger
 */
@Extension
public class GnatParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -7139298560308123856L;

    /**
     * Creates a new instance of {@link GnatParser}.
     */
    public GnatParser() {
        super(Messages._Warnings_gnat_ParserName(),
                Messages._Warnings_gnat_LinkName(),
                Messages._Warnings_gnat_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.GnatParser();
    }
}
