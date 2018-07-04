package hudson.plugins.warnings.parser;

import org.json.JSONObject;

import hudson.Extension;

/**
 * Class which parses SonarQube reports taken from Sonarqube differential scan report (preview).
 *
 * @author Carles Capdevila <Carles.Capdevila@t-systems.com>
 */
@Extension
public class SonarQubeDiffParser extends SonarQubeParser {
    private static final long serialVersionUID = -47634856667313368L;

    private static final String ISSUE_IS_NEW = "isNew";
    private static final String COMPONENT_MODULE_KEY = "moduleKey";

    /**
     * Creates a new instance of {@link SonarQubeIssuesParser}.
     */
    public SonarQubeDiffParser() {
        super(Messages._Warnings_SonarQubeDiff_ParserName(),
                Messages._Warnings_SonarQubeDiff_LinkName(),
                Messages._Warnings_SonarQubeDiff_Trend());
    }

    /** {@inheritDoc} */
    @Override
    public boolean issueFilter(final JSONObject issue) {
        return issue.optBoolean(ISSUE_IS_NEW, false);
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
            String modulePath = parseModulePath(component, COMPONENT_MODULE_KEY);
            return modulePath + filePath;
        } else {
            return super.parseFilename(issue);
        }
    }

}
