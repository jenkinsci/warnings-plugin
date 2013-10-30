package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the erlc compiler warnings.
 *
 * @author Stefan Brausch
 */
@Extension
public class ErlcParser extends AbstractWarningsParser {
    private static final long serialVersionUID = 8986478184830773892L;

    /**
     * Creates a new instance of {@link ErlcParser}.
     */
    public ErlcParser() {
        super(Messages._Warnings_Erlang_ParserName(),
                Messages._Warnings_Erlang_LinkName(),
                Messages._Warnings_Erlang_TrendName());
    }

    @Override
    protected String getId() {
        return "Erlang Compiler";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.ErlcParser();
    }
}

