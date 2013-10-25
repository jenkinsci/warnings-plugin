package hudson.plugins.warnings.parser;

import hudson.Extension;

import hudson.plugins.warnings.WarningsDescriptor;

/**
 * A parser for the javac compiler warnings.
 *
 * @author Ulli Hafner
 */
@Extension
public class JavacParser extends AbstractWarningsParser {
    static final String JAVA_SMALL_ICON = WarningsDescriptor.IMAGE_PREFIX + "java-24x24.png";
    static final String JAVA_LARGE_ICON = WarningsDescriptor.IMAGE_PREFIX + "java-48x48.png";

    private static final long serialVersionUID = 7199325311690082782L;

    /**
     * Creates a new instance of {@link JavacParser}.
     */
    public JavacParser() {
        super(Messages._Warnings_JavaParser_ParserName(),
                Messages._Warnings_JavaParser_LinkName(),
                Messages._Warnings_JavaParser_TrendName());
    }

    @Override
    public String getSmallImage() {
        return JAVA_SMALL_ICON;
    }

    @Override
    public String getLargeImage() {
        return JAVA_LARGE_ICON;
    }

    @Override
    protected String getId() {
        return "Java Compiler"; // old ID in serialization
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.JavacParser();
    }
}

