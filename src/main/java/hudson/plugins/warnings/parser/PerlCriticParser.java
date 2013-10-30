package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Perl::Critic warnings.
 *
 * @author Mihail Menev, menev@hm.edu
 */
@Extension
public class PerlCriticParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -6481203155449490873L;

    /**
     * Creates a new instance of {@link PerlCriticParser}.
     */
    public PerlCriticParser() {
        super(Messages._Warnings_PerlCritic_ParserName(),
                Messages._Warnings_PerlCritic_LinkName(),
                Messages._Warnings_PerlCritic_TrendName());
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.PerlCriticParser();
    }
}