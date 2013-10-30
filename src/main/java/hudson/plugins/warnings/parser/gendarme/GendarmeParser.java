package hudson.plugins.warnings.parser.gendarme;

import hudson.Extension;

import hudson.plugins.warnings.parser.AbstractWarningsParser;
import hudson.plugins.warnings.parser.Messages;

/**
 * Parses Gendarme violations.
 *
 * @author mathias.kluba@gmail.com
 */
@Extension
public class GendarmeParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 1677715364464119907L;

    /**
     * Creates a new instance of {@link GendarmeParser}.
     */
    public GendarmeParser() {
        super(Messages._Warnings_Gendarme_ParserName(),
                Messages._Warnings_Gendarme_LinkName(),
                Messages._Warnings_Gendarme_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.gendarme.GendarmeParser();
    }
}
