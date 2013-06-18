package hudson.plugins.warnings.parser;

import java.util.List;

import hudson.plugins.violations.types.codenarc.CodenarcParser;
import hudson.plugins.violations.types.csslint.CssLintParser;
import hudson.plugins.violations.types.gendarme.GendarmeParser;
import hudson.plugins.violations.types.stylecop.StyleCopParser;

/**
 * Registers the parsers of the violations plug-in.
 *
 * @author Ulli Hafner
 */
public final class ViolationsRegistry {
    /**
     * Appends the parsers of the violations plug-in to the specified list of
     * parsers.
     *
     * @param parsers
     *            the list of parsers that will be modified
     */
    public static void addParsers(final List<AbstractWarningsParser> parsers) {
        parsers.add(new ViolationsAdapter(new CodenarcParser(),
                Messages._Warnings_Codenarc_ParserName(),
                Messages._Warnings_Codenarc_LinkName(),
                Messages._Warnings_Codenarc_TrendName()));
        parsers.add(new ViolationsAdapter(new CssLintParser(),
                Messages._Warnings_CssLint_ParserName(),
                Messages._Warnings_CssLint_LinkName(),
                Messages._Warnings_CssLint_TrendName()));
        parsers.add(new ViolationsAdapter(new GendarmeParser(),
                Messages._Warnings_Gendarme_ParserName(),
                Messages._Warnings_Gendarme_LinkName(),
                Messages._Warnings_Gendarme_TrendName()));
        parsers.add(new ViolationsAdapter(new StyleCopParser(),
                Messages._Warnings_StyleCop_ParserName(),
                Messages._Warnings_StyleCop_LinkName(),
                Messages._Warnings_StyleCop_TrendName()));
    }

    /**
     * Creates a new instance of {@link ViolationsRegistry}.
     */
    private ViolationsRegistry() {
        // prevents instantiation
    }
}

