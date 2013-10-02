package com.ullihafner.warningsparser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.tools.ant.util.ReaderInputStream;
import org.xml.sax.SAXException;

/**
 * Class for parsers based on {@link JSLintXMLSaxParser}. Can parse CssLint and JSLint.
 *
 * @author Ulli Hafner
 */
public class LintParser extends AbstractWarningsParser {

    @Override
    public Collection<Warning> parse(final Reader file) throws IOException, ParsingCanceledException {
        try {
            List<Warning> warnings = new ArrayList<Warning>();
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();

            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(new ReaderInputStream(file, "UTF-8"), new JSLintXMLSaxParser(warnings));

            return warnings;
        }
        catch (SAXException exception) {
            throw new IOException(exception);
        }
        catch (ParserConfigurationException exception) {
            throw new IOException(exception);
        }
    }
}
