package com.ullihafner.warningsparser.jcreport;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ullihafner.warningsparser.AbstractWarningsParser;
import com.ullihafner.warningsparser.ParserException;
import com.ullihafner.warningsparser.ParsingCanceledException;
import com.ullihafner.warningsparser.Warning;
import com.ullihafner.warningsparser.Warning.Priority;

/**
 * JcReportParser-Class. This class parses from the jcReport.xml and creates warnings from its content.
 *
 * @author Johann Vierthaler, johann.vierthaler@web.de
 */
public class JcReportParser extends AbstractWarningsParser {
    /**
     * This overwritten method passes the reader to createReport() and starts adding all the warnings to the Collection
     * that will be returned at the end of the method.
     *
     * @return the collection of Warnings parsed from the Report.
     * @param reader
     *            the reader that parses from the source-file.
     * @exception IOException
     *                thrown by createReport()
     * @exception ParsingCanceledException
     *                thrown by createReport()
     */
    @Override
    public Collection<Warning> parse(final Reader reader) throws IOException, ParsingCanceledException {
        Report report = createReport(reader);
        List<Warning> warnings = new ArrayList<Warning>();

        for (int i = 0; i < report.getFiles().size(); i++) {
            File file = report.getFiles().get(i);

            for (int j = 0; j < file.getItems().size(); j++) {
                Item item = file.getItems().get(j);
                Warning warning = createWarning(file.getName(), getLineNumber(item.getLine()),
                        item.getFindingtype(), item.getMessage(), getPriority(item.getSeverity()));

                warning.setOrigin(item.getOrigin());
                warning.setPackageName(file.getPackageName());
                warning.setWorkspacePath(file.getSrcdir());
                warning.setColumnPosition(getLineNumber(item.getColumn()), getLineNumber(item.getEndcolumn()));
                warnings.add(warning);
            }
        }
        return warnings;
    }

    /**
     * The severity-level parsed from the JcReport will be matched with a priority.
     *
     * @param issueLevel
     *            the severity-level parsed from the JcReport.
     * @return the priority-enum matching with the issueLevel.
     */
    private Priority getPriority(final String issueLevel) {
        if (StringUtils.isEmpty(issueLevel)) {
            return Priority.HIGH;
        }

        if (issueLevel.contains("CriticalError")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("Error")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("CriticalWarning")) {
            return Priority.HIGH;
        }
        else if (issueLevel.contains("Warning")) {
            return Priority.NORMAL;
        }
        else {
            return Priority.LOW;
        }
    }

    /**
     * Creates a Report-Object out of the content within the JcReport.xml.
     *
     * @param source
     *            the Reader-object that is the source to build the Report-Object.
     * @return the finished Report-Object that creates the Warnings.
     * @throws IOException
     *              due to digester.parse(new InputSource(source))
     */
    public Report createReport(final Reader source) throws IOException {
        try {
            DigesterLoader digesterLoader = DigesterLoader.newLoader(new JcReportModule());
            digesterLoader.setClassLoader(JcReportModule.class.getClassLoader());

            Digester digester = digesterLoader.newDigester();
            return digester.parse(new InputSource(source));
        }

        catch (SAXException exception) {
            throw new ParserException(exception);
        }
    }
}
