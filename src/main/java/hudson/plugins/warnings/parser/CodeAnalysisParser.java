package hudson.plugins.warnings.parser;

import org.jvnet.localizer.Localizable;

import hudson.Extension;

/**
 * A parser for the CodeAnalysis compiler warnings.
 *
 * @author Rafal Jasica
 */
@Extension
public class CodeAnalysisParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -125874563249851L;

    /**
     * Creates a new instance of {@link CodeAnalysisParser}.
     */
    public CodeAnalysisParser() {
        this(Messages._Warnings_CodeAnalysis_ParserName(), Messages._Warnings_CodeAnalysis_LinkName(),
                Messages._Warnings_CodeAnalysis_TrendName());
    }

    /**
     * Creates a new instance of {@link CodeAnalysisParser}.
     *
     * @param parserName
     *            name of the parser
     * @param linkName
     *            name of the project action link
     * @param trendName
     *            name of the trend graph
     */
    public CodeAnalysisParser(final Localizable parserName, final Localizable linkName, final Localizable trendName) {
        super(parserName, linkName, trendName);
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.CodeAnalysisParser();
    }
}
