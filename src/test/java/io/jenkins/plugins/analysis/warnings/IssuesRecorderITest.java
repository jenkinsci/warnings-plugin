package io.jenkins.plugins.analysis.warnings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule.WebClient;
import org.jvnet.hudson.test.recipes.WithTimeout;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

import io.jenkins.plugins.analysis.core.model.AnalysisResult;
import static io.jenkins.plugins.analysis.core.model.Assertions.*;
import io.jenkins.plugins.analysis.core.model.StaticAnalysisTool;
import io.jenkins.plugins.analysis.core.steps.IssuesRecorder;
import io.jenkins.plugins.analysis.core.steps.ToolConfiguration;
import io.jenkins.plugins.analysis.core.testutil.IntegrationTest;
import io.jenkins.plugins.analysis.core.views.ResultAction;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;

/**
 * Integration tests of the warnings plug-in in freestyle jobs. Tests the new recorder {@link IssuesRecorder}.
 *
 * @author Ullrich Hafner
 */
public class IssuesRecorderITest extends IntegrationTest {
    /**
     * Runs the Eclipse parser on an empty workspace: the build should report 0 issues and an error message.
     */
    @Test
    public void shouldCreateEmptyResult() {
        FreeStyleProject project = createJob();
        enableWarnings(project);

        AnalysisResult result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);

        assertThat(result).hasTotalSize(0);
        assertThat(result).hasErrorMessages("No files found for pattern '**/*issues.txt'. Configuration error?");
    }

    /**
     * Runs the Eclipse parser on an output file that contains several issues: the build should report 8 issues.
     */
    @Test
    public void shouldCreateResultWithWarnings() {
        FreeStyleProject project = createJobWithWorkspaceFile("eclipse.txt");
        enableWarnings(project);

        AnalysisResult result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);

        assertThat(result).hasTotalSize(8);
        assertThat(result).hasInfoMessages("Resolved module names for 8 issues",
                "Resolved package names of 4 affected files");
    }

    /**
     * Sets the UNSTABLE threshold to 8 and parse a file that contains exactly 8 warnings: the build should be
     * unstable.
     */
    @Test
    public void shouldCreateUnstableResult() {
        FreeStyleProject project = createJobWithWorkspaceFile("eclipse.txt");
        enableWarnings(project, publisher -> publisher.setUnstableTotalAll(7));

        AnalysisResult result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);

        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        HtmlPage page = getWebPage(result);
        assertThat(page.getElementsByIdAndOrName("statistics")).hasSize(1);
    }

    private HtmlPage getWebPage(final AnalysisResult result) {
        try {
            WebClient webClient = j.createWebClient();
            webClient.setJavaScriptEnabled(false);
            return webClient.getPage(result.getOwner(), "eclipseResult");
        }
        catch (SAXException | IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a new {@link FreeStyleProject freestyle job}. The job will get a generated name.
     *
     * @return the created job
     */
    private FreeStyleProject createJob() {
        try {
            return j.createFreeStyleProject();
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a new {@link FreeStyleProject freestyle job} and copies the specified resources to the workspace folder.
     * The job will get a generated name.
     *
     * @param fileNames
     *         the files to copy to the workspace
     *
     * @return the created job
     */
    private FreeStyleProject createJobWithWorkspaceFile(final String... fileNames) {
        FreeStyleProject job = createJob();
        copyFilesToWorkspace(job, fileNames);
        return job;
    }

    /**
     * Enables the warnings plugin for the specified job. I.e., it registers a new {@link IssuesRecorder } recorder for
     * the job.
     *
     * @param job
     *         the job to register the recorder for
     *
     * @return the created recorder
     */
    @CanIgnoreReturnValue
    private IssuesRecorder enableWarnings(final FreeStyleProject job) {
        IssuesRecorder publisher = new IssuesRecorder();
        publisher.setTools(Collections.singletonList(new ToolConfiguration("**/*issues.txt", new Eclipse())));
        job.getPublishersList().add(publisher);
        return publisher;
    }

    /**
     * Enables the warnings plugin for the specified job. I.e., it registers a new {@link IssuesRecorder } recorder for
     * the job.
     *
     * @param job
     *         the job to register the recorder for
     * @param isAggregationEnabled
     *         is aggregation enabled?
     * @param toolPattern1
     *         the first new filename in the workspace
     * @param tool1
     *         class of the first tool
     * @param toolPattern2
     *         the second new filename in the workspace
     * @param tool2
     *         class of the second tool
     */
    @CanIgnoreReturnValue
    private void enableWarningsAggregation(final FreeStyleProject job, final boolean isAggregationEnabled,
            final String toolPattern1, final StaticAnalysisTool tool1, final String toolPattern2,
            final StaticAnalysisTool tool2) {
        IssuesRecorder publisher = new IssuesRecorder();
        publisher.setAggregatingResults(isAggregationEnabled);
        List<ToolConfiguration> toolList = new ArrayList<>();
        toolList.add(new ToolConfiguration(toolPattern1, tool1));
        toolList.add(new ToolConfiguration(toolPattern2, tool2));
        publisher.setTools(toolList);

        job.getPublishersList().add(publisher);
    }

    /**
     * Enables the warnings plugin for the specified job. I.e., it registers a new {@link IssuesRecorder } recorder for
     * the job.
     *
     * @param job
     *         the job to register the recorder for
     * @param configuration
     *         configuration of the recorder
     *
     * @return the created recorder
     */
    @CanIgnoreReturnValue
    private IssuesRecorder enableWarnings(final FreeStyleProject job, final Consumer<IssuesRecorder> configuration) {
        IssuesRecorder publisher = enableWarnings(job);
        configuration.accept(publisher);
        return publisher;
    }

    /**
     * Schedules a new build for the specified job and returns the created {@link AnalysisResult} after the build has
     * been finished.
     *
     * @param job
     *         the job to schedule
     * @param status
     *         the expected result for the build
     *
     * @return the created {@link ResultAction}
     */
    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock"})
    private AnalysisResult scheduleBuildAndAssertStatus(final FreeStyleProject job, final Result status) {
        try {
            FreeStyleBuild build = j.assertBuildStatus(status, job.scheduleBuild2(0));

            ResultAction action = build.getAction(ResultAction.class);

            assertThat(action).isNotNull();

            return action.getResult();
        }
        catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Schedules a new build for the specified job and returns the created {@link List<AnalysisResult>} after the build
     * has been finished as one result of both tools.
     *
     * @param job
     *         the job to schedule
     * @param status
     *         the expected result of both tools for the build
     *
     * @return the created {@link List<ResultAction>}
     */
    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock"})
    private List<AnalysisResult> scheduleBuildAndAssertStatusForBothTools(final FreeStyleProject job,
            final Result status) {
        try {
            FreeStyleBuild build = j.assertBuildStatus(status, job.scheduleBuild2(0));

            List<ResultAction> actions = build.getActions(ResultAction.class);

            List<AnalysisResult> results = new ArrayList<>();
            for (ResultAction elements : actions) {
                results.add(elements.getResult());
            }
            return results;
        }
        catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Schedules a new build for the specified job and checks the console log.
     *
     * @param job
     *         the job to schedule
     * @param status
     *         the expected result of both tools for the build
     * @param log
     *         the log string asserted to be in console
     *
     * @return the created {@link FreeStyleBuild}
     */
    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock"})
    private FreeStyleBuild scheduleBuildAndAssertLog(final FreeStyleProject job, final Result status, final String log) {
        try {
            FreeStyleBuild build = j.assertBuildStatus(status, job.scheduleBuild2(0));
            j.assertLogContains(log, build);
            return build;
        }
        catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Creates a {@link List<AnalysisResult>} of the analysis results of {@link FreeStyleBuild}.
     *
     * @param build
     *         the FreeStyleBuild
     *
     * @return the created {@link List<ResultAction>}
     */
    @SuppressWarnings({"illegalcatch", "OverlyBroadCatchBlock"})
    private List<AnalysisResult> getAssertStatusForBothTools(final FreeStyleBuild build) {
        try {
            List<ResultAction> actions = build.getActions(ResultAction.class);

            List<AnalysisResult> results = new ArrayList<>();
            for (ResultAction elements : actions) {
                results.add(elements.getResult());
            }
            return results;
        }
        catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Runs the CheckStyle and PMD tools for two corresponding files which contain at least 6 respectively 4 issues: the
     * build should report 6 and 4 issues.
     */
    @Test
    @WithTimeout(1000)
    public void shouldCreateMultipleToolsAndAggregationResultWithWarningsAggregateFalse() {
        FreeStyleProject project = createJobWithWorkspaceFile("checkstyle.xml", "pmd-warnings.xml");
        enableWarningsAggregation(project, false, "**/checkstyle-issues.txt", new CheckStyle(),
                "**/pmd-warnings-issues.txt", new Pmd());

        List<AnalysisResult> results = scheduleBuildAndAssertStatusForBothTools(project, Result.SUCCESS);

        assertThat(results).hasSize(2);

        for (AnalysisResult element : results) {
            if (element.getId().equals("checkstyle")) {
                assertThat(element).hasTotalSize(6);
            }
            else {
                assertThat(element.getId()).isEqualTo("pmd");
                assertThat(element).hasTotalSize(4);
            }
            assertThat(element).hasOverallResult(Result.SUCCESS);
        }
    }

    /**
     * Runs the CheckStyle and PMD tools for two corresponding files which contain at least 6 respectively 4 issues: due
     * to enabled aggregation, the build should report 10 issues.
     */
    @Test
    @WithTimeout(1000)
    public void shouldCreateMultipleToolsAndAggregationResultWithWarningsAggregateTrue() {
        FreeStyleProject project = createJobWithWorkspaceFile("checkstyle.xml", "pmd-warnings.xml");
        enableWarningsAggregation(project, true, "**/checkstyle-issues.txt", new CheckStyle(),
                "**/pmd-warnings-issues.txt", new Pmd());

        List<AnalysisResult> results = scheduleBuildAndAssertStatusForBothTools(project, Result.SUCCESS);

        assertThat(results).hasSize(1);

        for (AnalysisResult element : results) {
            assertThat(element.getSizePerOrigin()).containsKeys("checkstyle", "pmd");
            assertThat(element).hasTotalSize(10);
            assertThat(element).hasId("analysis");
            assertThat(element).hasOverallResult(Result.SUCCESS);
        }
    }

    /**
     * Runs the CheckStyle tool twice for two different files with varying amount of issues: should produce a failure.
     */
    @Test
    @WithTimeout(1000)
    public void shouldCreateMultipleToolsAndAggregationResultWithWarningsAggregateFalseAndSameTool() {
        FreeStyleProject project = createJobWithWorkspaceFile("checkstyle2.xml", "checkstyle3.xml");
        enableWarningsAggregation(project, false, "**/checkstyle2-issues.txt", new CheckStyle(),
                "**/checkstyle3-issues.txt", new CheckStyle());

        FreeStyleBuild build = scheduleBuildAndAssertLog(project, Result.FAILURE,"ID checkstyle is already used by another action: io.jenkins.plugins.analysis.core.views.ResultAction for CheckStyle");

        List<AnalysisResult> results = getAssertStatusForBothTools(build);

        assertThat(results).hasSize(1);

        for (AnalysisResult element : results) {
            assertThat(element).hasId("checkstyle");
        }
    }

    /**
     * Runs the CheckStyle tool twice for two different files with varying amount of issues: due to enabled aggregation,
     * the build should report 6 issues.
     */
    @Test
    @WithTimeout(1000)
    public void shouldCreateMultipleToolsAndAggregationResultWithWarningsAggregateTrueAndSameTool() {
        FreeStyleProject project = createJobWithWorkspaceFile("checkstyle2.xml", "checkstyle3.xml");
        enableWarningsAggregation(project, true, "**/checkstyle2-issues.txt", new CheckStyle(),
                "**/checkstyle3-issues.txt", new CheckStyle());

        List<AnalysisResult> results = scheduleBuildAndAssertStatusForBothTools(project, Result.SUCCESS);

        assertThat(results).hasSize(1);

        for (AnalysisResult element : results) {
            assertThat(element.getSizePerOrigin()).containsKeys("checkstyle");
            assertThat(element).hasTotalSize(6);
            assertThat(element).hasId("analysis");
            assertThat(element).hasOverallResult(Result.SUCCESS);
        }
    }
}