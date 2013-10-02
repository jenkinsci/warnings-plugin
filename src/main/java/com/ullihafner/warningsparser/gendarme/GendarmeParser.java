package com.ullihafner.warningsparser.gendarme;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ullihafner.warningsparser.AbstractWarningsParser;
import com.ullihafner.warningsparser.ParsingCanceledException;
import com.ullihafner.warningsparser.Warning;
import com.ullihafner.warningsparser.Warning.Priority;
import com.ullihafner.warningsparser.util.XmlElementUtil;

/**
 * Parses Gendarme violations.
 *
 * @author mathias.kluba@gmail.com
 */
public class GendarmeParser extends AbstractWarningsParser {
    private static final Pattern FILE_PATTERN = Pattern.compile("^(.*)\\(.(\\d+)\\).*$");

    /** {@inheritDoc} */
    @Override
    public Collection<Warning> parse(final Reader reader) throws IOException, ParsingCanceledException {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(reader));

            NodeList mainNode = doc.getElementsByTagName("gendarme-output");

            Element rootElement = (Element)mainNode.item(0);
            Element resultsElement = (Element)rootElement.getElementsByTagName("results").item(0);
            Element rulesElement = (Element)rootElement.getElementsByTagName("rules").item(0);

            Map<String, GendarmeRule> rules = parseRules(XmlElementUtil.getNamedChildElements(rulesElement, "rule"));
            return parseViolations(XmlElementUtil.getNamedChildElements(resultsElement, "rule"), rules);
        }
        catch (ParserConfigurationException pce) {
            throw new IOException(pce);
        }
        catch (SAXException se) {
            throw new IOException(se);
        }
    }

    private List<Warning> parseViolations(final List<Element> ruleElements, final Map<String, GendarmeRule> rules) {
        List<Warning> warnings = new ArrayList<Warning>();
        for (Element ruleElement : ruleElements) {
            String ruleName = ruleElement.getAttribute("Name");
            String problem = ruleElement.getElementsByTagName("problem").item(0).getTextContent();
            List<Element> targetElements = XmlElementUtil.getNamedChildElements(ruleElement, "target");

            GendarmeRule rule = rules.get(ruleName);
            for (Element targetElement : targetElements) {
                Element defectElement = (Element)targetElement.getElementsByTagName("defect").item(0);
                String source = defectElement.getAttribute("Source");

                String fileName = extractFileNameMatch(rule, source, 1);
                Priority priority = extractPriority(defectElement);
                int line = convertLineNumber(extractFileNameMatch(rule, source, 2));

                warnings.add(createWarning(fileName, line, rule.getName(), problem, priority));
            }
        }
        return warnings;
    }

    private Priority extractPriority(final Element defectElement) {
        String severityString = defectElement.getAttribute("Severity");
        Priority priority;
        if ("Low".equals(severityString)) {
            priority = Priority.LOW;
        }
        else if ("High".equals(severityString)) {
            priority = Priority.HIGH;
        }
        else {
            priority = Priority.NORMAL;
        }
        return priority;
    }

    private String extractFileNameMatch(final GendarmeRule rule, final String source, final int group) {
        String fileName = StringUtils.EMPTY;
        if (rule.getType() == GendarmeRuleType.Method) {
            Matcher matcher = FILE_PATTERN.matcher(source);
            if (matcher.matches()) {
                fileName = matcher.group(group);
            }
        }
        return fileName;
    }

    private Map<String, GendarmeRule> parseRules(final List<Element> ruleElements) {
        Map<String, GendarmeRule> rules = new HashMap<String, GendarmeRule>();

        for (Element ruleElement : ruleElements) {
            GendarmeRule rule = new GendarmeRule();
            rule.setName(ruleElement.getAttribute("Name"));
            rule.setTypeName(ruleElement.getTextContent());

            String typeString = ruleElement.getAttribute("Type");
            if ("Type".equals(typeString)) {
                rule.setType(GendarmeRuleType.Type);
            }
            else if ("Method".equals(typeString)) {
                rule.setType(GendarmeRuleType.Method);
            }
            else if ("Assembly".equals(typeString)) {
                rule.setType(GendarmeRuleType.Assembly);
            }
            try {
                rule.setUrl(new URL(ruleElement.getAttribute("Uri")));
            }
            catch (MalformedURLException e) {
                rule.setUrl(null);
            }

            // add the rule to the cache
            rules.put(rule.getName(), rule);
        }

        return rules;
    }
}
