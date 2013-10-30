package hudson.plugins.warnings.parser;

import hudson.Extension;

/**
 * A parser for messages from the NAG Fortran Compiler.
 *
 * @author Mat Cross.
 */
@Extension
public class NagFortranParser extends AbstractWarningsParser {
  private static final long serialVersionUID = 0L;

  /**
   * Creates a new instance of {@link NagFortranParser}.
   */
  public NagFortranParser() {
    super(Messages._Warnings_NagFortran_ParserName(),
          Messages._Warnings_NagFortran_LinkName(),
          Messages._Warnings_NagFortran_TrendName());
  }

  @Override
  protected String getId() {
    return "NAG Fortran Compiler (nagfor)";
  }

  @Override
  protected com.ullihafner.warningsparser.WarningsParser getParser() {
      return new com.ullihafner.warningsparser.NagFortranParser();
  }
}
