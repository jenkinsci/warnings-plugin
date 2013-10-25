package hudson.plugins.warnings.parser;

import org.jvnet.localizer.Localizable;

import hudson.Extension;

/**
 * A parser for the MSBuild/PcLint compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class MsBuildParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -2141974437420906595L;

    /**
     * Creates a new instance of {@link MsBuildParser}.
     */
    public MsBuildParser() {
        this(Messages._Warnings_MSBuild_ParserName(),
                Messages._Warnings_MSBuild_LinkName(),
                Messages._Warnings_MSBuild_TrendName());
    }

    /**
     * Creates a new instance of {@link MsBuildParser}.
     *
     * @param parserName
     *            name of the parser
     * @param linkName
     *            name of the project action link
     * @param trendName
     *            name of the trend graph
     */
    public MsBuildParser(final Localizable parserName, final Localizable linkName, final Localizable trendName) {
        super(parserName, linkName, trendName);
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.MsBuildParser();
    }
}

