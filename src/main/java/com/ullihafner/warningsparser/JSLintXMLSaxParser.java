package com.ullihafner.warningsparser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.ullihafner.warningsparser.Warning.Priority;

/**
 * Handles parsing.
 */
public class JSLintXMLSaxParser extends DefaultHandler {
    private final List<Warning> warnings;
    private String fileName;

    /* Categories. */
    public static final String CATEGORY_PARSING = "Parsing";
    public static final String CATEGORY_UNDEFINED_VARIABLE = "Undefined Variable";
    static final String CATEGORY_FORMATTING = "Formatting";

    /**
     * Creates a new instance of {@link JSLintXMLSaxParser}.
     *
     * @param warnings
     *            the warnings output
     */
    public JSLintXMLSaxParser(final List<Warning> warnings) {
        super();

        this.warnings = warnings;
    }

    @Override
    public void startElement(final String namespaceURI, final String localName, final String qName,
            final Attributes atts) throws SAXException {
        String key = qName;

        if (isLintDerivate(key)) {
            return; // Start element, good to skip
        }

        if ("file".equals(key)) {
            fileName = atts.getValue("name");
            return;
        }

        if ("issue".equals(key) || "error".equals(key)) {
            createWarning(atts);
        }
    }

    private void createWarning(final Attributes attributes) {
        String category = StringUtils.EMPTY;
        Priority priority = Priority.NORMAL;

        String message = extractFrom(attributes, "reason", "message");
        if (message.startsWith("Expected")) {
            priority = Priority.HIGH;
            category = CATEGORY_PARSING;
        }
        else if (message.endsWith(" is not defined.")) {
            priority = Priority.HIGH;
            category = CATEGORY_UNDEFINED_VARIABLE;
        }
        else if (message.contains("Mixed spaces and tabs")) {
            priority = Priority.LOW;
            category = CATEGORY_FORMATTING;
        }
        else if (message.contains("Unnecessary semicolon")) {
            category = CATEGORY_FORMATTING;
        }
        else if (message.contains("is better written in dot notation")) {
            category = CATEGORY_FORMATTING;
        }

        int lineNumber = AbstractWarningsParser.convertLineNumber(attributes.getValue("line"));
        Warning warning = new Warning(fileName, lineNumber, category, message, priority);

        String column = extractFrom(attributes, "column", "char");
        if (StringUtils.isNotBlank(column)) {
            warning.setColumnPosition(AbstractWarningsParser.convertLineNumber(column));
        }
        warnings.add(warning);
    }

    private String extractFrom(final Attributes atts, final String first, final String second) {
        String value = atts.getValue(first);
        if (StringUtils.isEmpty(value)) {
            value = atts.getValue(second);
        }
        return value;
    }

    private boolean isLintDerivate(final String key) {
        return key != null && key.contains("lint");
    }
}
