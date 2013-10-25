package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the ant JavaDoc compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class JavaDocParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 7127568148333474921L;

    /**
     * Creates a new instance of {@link JavaDocParser}.
     */
    public JavaDocParser() {
        super(Messages._Warnings_JavaDoc_ParserName(),
                Messages._Warnings_JavaDoc_LinkName(),
                Messages._Warnings_JavaDoc_TrendName());
    }

    @Override
    protected String getId() {
        return "JavaDoc";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.JavaDocParser();
    }
}

