package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the ant javac compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class AntJavacParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 1737791073711198075L;

    /**
     * Creates a new instance of {@link AntJavacParser}.
     */
    public AntJavacParser() {
        super(Messages._Warnings_JavaParser_ParserName(),
                Messages._Warnings_JavaParser_LinkName(),
                Messages._Warnings_JavaParser_TrendName());
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
    protected String getId() {
        return "Java Compiler"; // old ID in serialization
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.AntJavacParser();
    }

}

