package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for PHP runtime errors and warnings.
 *
 * @author Shimi Kiviti
 */
@Extension
public class PhpParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -5154327854315791181L;

    /**
     * Creates a new instance of {@link PhpParser}.
     */
    public PhpParser() {
        super(Messages._Warnings_PHP_ParserName(),
                Messages._Warnings_PHP_LinkName(),
                Messages._Warnings_PHP_TrendName());
    }

    @Override
    protected String getId() {
        return "PHP Runtime Warning";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.PhpParser();
    }
}