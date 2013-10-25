package hudson.plugins.warnings.parser;


/**
 * A multi-line parser that uses a configurable regular expression and Groovy script to parse warnings.
 *
 * @author Ulli Hafner
 */
public class DynamicDocumentParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -690643673847390322L;

    private final String regexp;
    private final String script;

    /**
     * Creates a new instance of {@link DynamicDocumentParser}.
     *
     * @param name
     *            name of the parser
     * @param regexp
     *            regular expression
     * @param script
     *            Groovy script
     * @param linkName
     *            the name of the ProjectAction (link name)
     * @param trendName
     *            the name of the trend report
     */
    public DynamicDocumentParser(final String name, final String regexp, final String script, final String linkName,
            final String trendName) {
        super(localize(name), localize(linkName), localize(trendName));

        this.regexp = regexp;
        this.script = script;
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.DynamicDocumentParser(regexp, script);
    }
}
