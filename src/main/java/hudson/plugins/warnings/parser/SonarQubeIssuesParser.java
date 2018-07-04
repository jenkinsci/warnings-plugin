package hudson.plugins.warnings.parser;

import org.json.JSONObject;

import hudson.Extension;

/**
 * Class which parses SonarQube reports taken from the Sonarqube API (api/issues/search).
 *
 * @author Carles Capdevila <Carles.Capdevila@t-systems.com>
 */
@Extension
public class SonarQubeIssuesParser extends SonarQubeParser {
    private static final long serialVersionUID = -8213765181968340929L;

    private static final String ISSUE_SUB_PROJECT= "subProject";

    /**
     * Creates a new instance of {@link SonarQubeIssuesParser}.
     */
    public SonarQubeIssuesParser() {
        super(Messages._Warnings_SonarQubeIssues_ParserName(),
                Messages._Warnings_SonarQubeIssues_LinkName(),
                Messages._Warnings_SonarQubeIssues_Trend());
    }

    @Override
    public String parseFilename(final JSONObject issue) {
        //Get component
        String componentKey = issue.optString(ISSUE_COMPONENT, null);
        JSONObject component = findComponentByKey(componentKey);

        if (component != null) {
            //Get file path inside module
            String filePath = component.optString(COMPONENT_PATH);

            //Get module file path
            String modulePath = parseModulePath(issue, ISSUE_SUB_PROJECT);
            return modulePath + filePath;
        } else {
            return super.parseFilename(issue);
        }
    }

}
