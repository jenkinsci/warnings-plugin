package hudson.plugins.warnings.parser;

import java.util.regex.Matcher;

import hudson.plugins.warnings.WarningsDescriptor;
import org.apache.commons.lang.StringEscapeUtils;

import hudson.Extension;

import hudson.plugins.analysis.util.model.Priority;

/**
 * A parser for IntelliJ IDEA inspections.
 *
 * @author Alex Lopashev
 */
@Extension
public class IdeaInspectionParser extends RegexpDocumentParser {
    private static final long serialVersionUID = 2001559489005510484L;

    private static final String IDEA_SMALL_ICON = WarningsDescriptor.IMAGE_PREFIX + "idea-24x24.png";
    private static final String IDEA_LARGE_ICON = WarningsDescriptor.IMAGE_PREFIX + "idea-48x48.png";

    private static final String IDEA_INSPECTION_PATTERN = "(?s)<problem>.*?<file>(.*?)</file>.*?<line>(.*?)</line>.*?<problem_class.*?severity=\"(.*?)\".*?>(.*?)</problem_class>.*?<description>(.*?)</description>.*?</problem>";

    /**
     * Creates a new instance of {@link IdeaInspectionParser}.
     */
    public IdeaInspectionParser() {
        super(Messages._Warnings_IdeaInspection_ParserName(),
                Messages._Warnings_IdeaInspection_LinkName(),
                Messages._Warnings_IdeaInspection_TrendName(),
                IDEA_INSPECTION_PATTERN, true);
    }

    @Override
    protected String getId() {
        return "IntelliJ IDEA Inspections";
    }

    @Override
    public String getSmallImage() {
        return IDEA_SMALL_ICON;
    }

    @Override
    public String getLargeImage() {
        return IDEA_LARGE_ICON;
    }

    @Override
    protected Warning createWarning(final Matcher matcher) {
        final String severity = matcher.group(3);
        final Priority priority = severity.equals("ERROR") ? Priority.HIGH : severity.equals("WARNING") ? Priority.NORMAL : Priority.LOW;
        return createWarning(
                matcher.group(1),
                getLineNumber(matcher.group(2)),
                StringEscapeUtils.unescapeXml(matcher.group(4)),
                StringEscapeUtils.unescapeXml(matcher.group(5)),
                priority
        );
    }
}

