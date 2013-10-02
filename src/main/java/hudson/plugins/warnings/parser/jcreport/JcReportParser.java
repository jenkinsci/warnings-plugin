package hudson.plugins.warnings.parser.jcreport;

import hudson.Extension;

import hudson.plugins.warnings.parser.AbstractWarningsParser;
import hudson.plugins.warnings.parser.Messages;

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
@Extension
public class JcReportParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -1302787609831475403L;

    /**
     * Creates a new instance of {@link JcReportParser}.
     */
    public JcReportParser() {
        super(Messages._Warnings_JCReport_ParserName(),
                Messages._Warnings_JCReport_LinkName(),
                Messages._Warnings_JCReport_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.jcreport.JcReportParser();
    }
}
