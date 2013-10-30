package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for the Clang compiler warnings.
 *
 * @author Neil Davis
 */
@Extension
public class ClangParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -3015592762345283182L;

    /**
     * Creates a new instance of {@link ClangParser}.
     */
    public ClangParser() {
        super(Messages._Warnings_AppleLLVMClang_ParserName(),
                Messages._Warnings_AppleLLVMClang_LinkName(),
                Messages._Warnings_AppleLLVMClang_TrendName());
    }

    @Override
    protected String getId() {
        return "Apple LLVM Compiler (Clang)"; // old ID in serialization
    }

    @Override
    protected com.ullihafner.warningsparser.WarningsParser getParser() {
        return new com.ullihafner.warningsparser.ClangParser();
    }
}
