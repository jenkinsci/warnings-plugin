package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Eclipse compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class EclipseParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 425883472788422955L;

    /**
     * Creates a new instance of {@link EclipseParser}.
     */
    public EclipseParser() {
        super(Messages._Warnings_EclipseParser_ParserName(),
                Messages._Warnings_EclipseParser_LinkName(),
                Messages._Warnings_EclipseParser_TrendName());
    }

    @Override
    protected String getId() {
        return "Eclipse Java Compiler";
    }

    @Override
    public String getSmallImage() {
        return JavacParser.JAVA_SMALL_ICON;
    }

    @Override
    public String getLargeImage() {
        return JavacParser.JAVA_LARGE_ICON;
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.EclipseParser();
    }
}

