package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for puppet-lint checks warnings.
 *
 * @author Jan Vansteenkiste <jan@vstone.eu>
 */
@Extension
public class PuppetLintParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 7492869677427430346L;

    /**
     * Creates a new instance of {@link PuppetLintParser}.
     */
    public PuppetLintParser() {
        super(Messages._Warnings_Puppet_ParserName(),
                Messages._Warnings_Puppet_LinkName(),
                Messages._Warnings_Puppet_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.PuppetLintParser();
    }
}

