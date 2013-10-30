package hudson.plugins.warnings.parser;

import org.jvnet.localizer.Localizable;

import com.ullihafner.warningsparser.JSLintXMLSaxParser;

/**
 * Base class for parsers based on {@link JSLintXMLSaxParser}.
 *
 * @author Ulli Hafner
 */
public abstract class LintParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 3341424685245834156L;

    /**
     * Creates a new instance of {@link LintParser}.
     *
     * @param parserName
     *            name of the parser
     * @param linkName
     *            name of the project action link
     * @param trendName
     *            name of the trend graph
     */
    protected LintParser(final Localizable parserName, final Localizable linkName, final Localizable trendName) {
        super(parserName, linkName, trendName);
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.LintParser();
    }
}
