package hudson.plugins.warnings.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;

import hudson.Extension;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.groovy.JsonSlurper;

/**
 * Class which parses SonarQube reports taken from the Sonarqube API (api/issues/search).
 *
 * @author Carles Capdevila <Carles.Capdevila@t-systems.com>
 */
@Extension
public class SonarqubeIssuesParser extends AbstractWarningsParser {
    private static final long serialVersionUID = -8213765181968340929L;

    private static final String COMPONENTS = "components";
    private static final String ISSUES = "issues";
    private static final String COMPONENT = "component";
    private static final String MESSAGE = "message";
    private static final String LINE = "line";
    private static final String SUB_PROJECT= "subProject";
    private static final String TYPE  = "type";
    private static final String SEVERITY = "severity";
    // Severity MAJOR is omitted below as it corresponds with default Priority: NORMAL
    private static final String SEVERITY_BLOCKER = "BLOCKER";
    private static final String SEVERITY_CRITICAL = "CRITICAL";
    private static final String SEVERITY_MINOR = "MINOR";
    private static final String SEVERITY_INFO = "INFO";

    private static final String COMPONENT_KEY = "key";
    private static final String COMPONENT_PATH = "path";


    /**
     * Creates a new instance of {@link SonarqubeIssuesParser}.
     */
    public SonarqubeIssuesParser() {
        super(Messages._Warnings_SonarQubeIssues_ParserName(),
                Messages._Warnings_SonarQubeIssues_LinkName(),
                Messages._Warnings_SonarQubeIssues_Trend());
    }

    @Override
    public Collection<FileAnnotation> parse(final Reader reader) throws IOException {
        List<FileAnnotation> warnings = new ArrayList<FileAnnotation>();

        JSON content = new JsonSlurper().parse(reader);


        if (content instanceof JSONObject) {
            JSONObject jsonReport = (JSONObject) content;
            //Get the components part to get the file paths on each issue (the component objects contain the most concise path)
            JSONArray components = null;
            if (jsonReport.containsKey(COMPONENTS) && jsonReport.get(COMPONENTS) instanceof JSONArray) {
                components = (JSONArray)jsonReport.get(COMPONENTS);
            }

            if (jsonReport.containsKey(ISSUES)) {
                JSONArray issues = jsonReport.getJSONArray(ISSUES);
                for (Object issueObj : issues) {
                    if (issueObj instanceof JSONObject) {
                        JSONObject issue = (JSONObject)issueObj;

                        //file
                        String modulePath = "";
                        if (issue.containsKey(SUB_PROJECT)) {
                            String subProject = issue.getString(SUB_PROJECT);
                            JSONObject moduleComponent = findComponentByKey(components, subProject);
                            if (moduleComponent != null && moduleComponent.containsKey(COMPONENT_PATH)) {
                                modulePath = moduleComponent.getString(COMPONENT_PATH) + "/";
                            }
                        }

                        String file = "";
                        String componentKey = issue.optString(COMPONENT, null);
                        JSONObject component = findComponentByKey(components, componentKey);
                        file = modulePath + component.optString(COMPONENT_PATH, componentKey); //Try to get the path. If not, default to componentKey

                        //line
                        int start = issue.optInt(LINE, -1);

                        //type
                        String type = issue.optString(TYPE, "");

                        //category - fixed category 'Sonarqube'

                        //message
                        String message = issue.getString(MESSAGE);

                        //priority
                        Priority priority = Priority.NORMAL;
                        String severity = issue.optString(SEVERITY, null);
                        // Severity MAJOR is omitted as it corresponds with default Priority: NORMAL
                        if (severity != null) {
                            if (severity.equals(SEVERITY_BLOCKER) || severity.equals(SEVERITY_CRITICAL)) {
                                priority = Priority.HIGH;
                            } else if (severity.equals(SEVERITY_MINOR) || severity.equals(SEVERITY_INFO)) {
                                priority = Priority.LOW;
                            }
                        }

                        warnings.add(createWarning(file, start, type, "Sonarqube", message, priority));
                    }
                }
            }
        }

        return warnings;
    }

    private JSONObject findComponentByKey (final JSONArray components, final String key) {
        if (components != null && key != null) {
            for (Object component : components) {
                if (component instanceof JSONObject) {
                    JSONObject jsonComponent = (JSONObject)component;
                    if (jsonComponent.getString(COMPONENT_KEY).equals(key)) {
                        return (JSONObject)component;
                    }
                }
            }
        }

        return null;
    }
}
