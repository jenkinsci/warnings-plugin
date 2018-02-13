package io.jenkins.plugins.analysis.warnings;

import javax.annotation.Nonnull;

import org.kohsuke.stapler.DataBoundConstructor;

import edu.hm.hafner.analysis.parser.TiCcsParser;
import io.jenkins.plugins.analysis.core.model.StaticAnalysisTool;

import hudson.Extension;

/**
 * Provides a parser and customized messages for the Texas Instruments Code Composer Studio compiler.
 *
 * @author Ullrich Hafner
 */
public class TiCss extends StaticAnalysisTool {
    static final String ID = "code-composer";

    /** Creates a new instance of {@link TiCss}. */
    @DataBoundConstructor
    public TiCss() {
        // empty constructor required for stapler
    }

    @Override
    public TiCcsParser createParser() {
        return new TiCcsParser();
    }

    /** Descriptor for this static analysis tool. */
    @Extension
    public static class Descriptor extends StaticAnalysisToolDescriptor {
        public Descriptor() {
            super(ID);
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.Warnings_TexasI_ParserName();
        }
    }
}