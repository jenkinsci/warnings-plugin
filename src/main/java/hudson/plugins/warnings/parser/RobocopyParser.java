package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for Robocopy.
 *
 * @author Adrian Deccico
 *
 * captured samples:
 *    *EXTRA File                  0        b           Unknown Task
 *   100%        New File                   0        a.log
 *                 same                 0        a.log
 */
@Extension
public class RobocopyParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -671744745118772873L;

    /**
     * Creates a new instance of {@link RobocopyParser}.
     */
    public RobocopyParser() {
        super(Messages._Warnings_Robocopy_ParserName(),
                Messages._Warnings_Robocopy_LinkName(),
                Messages._Warnings_Robocopy_TrendName());
    }

    @Override
    protected String getId() {
        return "Robocopy (please use /V in your commands!)";
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.RobocopyParser();
    }
}

