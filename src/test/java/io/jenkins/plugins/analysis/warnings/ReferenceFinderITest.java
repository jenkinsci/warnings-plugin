package io.jenkins.plugins.analysis.warnings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import io.jenkins.plugins.analysis.core.history.ReferenceFinder;
import io.jenkins.plugins.analysis.core.model.AnalysisResult;
import static io.jenkins.plugins.analysis.core.model.Assertions.*;
import io.jenkins.plugins.analysis.core.steps.IssuesRecorder;
import io.jenkins.plugins.analysis.core.steps.ToolConfiguration;
import io.jenkins.plugins.analysis.core.testutil.IntegrationTest;
import io.jenkins.plugins.analysis.core.views.ResultAction;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Shell;

/**
 * Integration tests of the warnings plug-in in freestyle jobs. Tests the new reference finder {@link ReferenceFinder}.
 *
 * @author Arne Sch�ntag
 */
public class ReferenceFinderITest extends IntegrationTest {

    private String jobName = "Job";
    private String refName = "RefJob";

    /**
     * Checks if the reference is taken from the last successful build and therefore returns a success in the end.
     */
    @Test
    public void shouldCreateSuccessResultWithIgnoredUnstableInBetween() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse2Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse8Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse4Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference is taken from the last successful build and therefore returns an unstable in the end.
     */
    @Test
    public void shouldCreateUnstableResultWithIgnoredUnstableInBetween() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse2Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse8Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference ignores the result of the last build and therefore returns a success in the end.
     */
    @Test
    public void shouldCreateSuccessResultWithNotIgnoredUnstableInBetween() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse2Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setIgnoreAnalysisResult(true);
            publisher.setUnstableNewAll(3);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse8Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference ignores the result of the last build and therefore returns an unstable in the end.
     */
    @Test
    public void shouldCreateUnstableResultWithNotIgnoredUnstableInBetween() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse6Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(project, publisher -> {
            publisher.setIgnoreAnalysisResult(true);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse4Warnings.txt");
        issuesRecorder.setUnstableTotalAll(3);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        project.getSomeWorkspace().deleteContents();
        issuesRecorder.setUnstableNewAll(3);
        issuesRecorder.setUnstableTotalAll(9);
        copyFilesToWorkspace(project, "eclipse8Warnings.txt");

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at complete success builds instead of just looking at the eclipse result.
     * Should return an unstable result.
     */
    @Test
    public void shouldCreateUnstableResultWithOverAllMustBeSuccess() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse2Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(project, publisher -> {
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
            publisher.setUnstableNewAll(3);
        });
        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse4Warnings.txt");
        Shell shell = new Shell("exit 1");
        project.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(project, Result.FAILURE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");
        project.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at complete success builds instead of just looking at the eclipse result.
     * Should return an success result.
     */
    @Test
    public void shouldCreateSuccessResultWithOverAllMustBeSuccess() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse4Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(project, publisher -> {
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });
        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse2Warnings.txt");
        issuesRecorder.setUnstableNewAll(3);
        Shell shell = new Shell("exit 1");
        project.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(project, Result.FAILURE);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");
        project.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference only looks at the eclipse result of a build and not the overall success. Should return an
     * unstable result.
     */
    @Test
    public void shouldCreateUnstableResultWithOverAllMustNotBeSuccess() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse4Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(project, publisher -> {
            publisher.setOverallResultMustBeSuccess(false);
            publisher.setEnabledForFailure(true);
        });
        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse2Warnings.txt");
        issuesRecorder.setUnstableNewAll(3);
        Shell shell = new Shell("exit 1");
        project.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(project, Result.FAILURE);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");
        project.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at the eclipse result of a build and not the overall success. Should return an
     * success result.
     */
    @Test
    public void shouldCreateSuccessResultWithOverAllMustNotBeSuccess() throws IOException, InterruptedException {
        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse2Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(project, publisher -> {
            publisher.setOverallResultMustBeSuccess(false);
            publisher.setEnabledForFailure(true);
            publisher.setUnstableNewAll(3);
        });
        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse4Warnings.txt");

        Shell shell = new Shell("exit 1");
        project.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(project, Result.FAILURE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        project.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(project, "eclipse6Warnings.txt");
        project.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    // With Reference Build

    /**
     * Checks if the reference is taken from the last successful build and therefore returns a success in the end. Uses
     * a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateSuccessResultWithIgnoredUnstableInBetweenWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse2Warnings.txt");
        enableWarnings(refJob, publisher -> {
            publisher.setUnstableNewAll(3);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse4Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setUnstableTotalAll(7);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse8Warnings.txt");

        result = scheduleBuildAndAssertStatus(refJob, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference is taken from the last successful build and therefore returns an unstable in the end.
     * Uses a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateUnstableResultWithIgnoredUnstableInBetweenWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse2Warnings.txt");
        enableWarnings(refJob, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setIgnoreAnalysisResult(false);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse8Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setIgnoreAnalysisResult(false);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse6Warnings.txt");

        result = scheduleBuildAndAssertStatus(refJob, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference ignores the result of the last build and therefore returns a success in the end. Uses a
     * different freestyle project for the reference.
     */
    @Test
    public void shouldCreateSeuccssResultWithNotIgnoredUnstableInBetweenWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse2Warnings.txt");
        enableWarnings(refJob, publisher -> {
            publisher.setIgnoreAnalysisResult(true);
            publisher.setUnstableNewAll(3);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse8Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setIgnoreAnalysisResult(true);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse6Warnings.txt");

        result = scheduleBuildAndAssertStatus(refJob, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference ignores the result of the last build and therefore returns an unstable in the end. Uses a
     * different freestyle project for the reference.
     */
    @Test
    public void shouldCreateUnstableResultWithNotIgnoredUnstableInBetweenWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse6Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(refJob, publisher -> {
            publisher.setIgnoreAnalysisResult(true);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse8Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setIgnoreAnalysisResult(true);
            publisher.setUnstableTotalAll(9);
        });

        AnalysisResult result;
        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse4Warnings.txt");
        issuesRecorder.setUnstableTotalAll(3);

        result = scheduleBuildAndAssertStatus(refJob, Result.UNSTABLE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.UNSTABLE);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(8);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at complete success builds instead of just looking at the eclipse result.
     * Should return an unstable result. Uses a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateUnstableResultWithOverAllMustBeSuccessWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse2Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(refJob, publisher -> {
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
            publisher.setUnstableNewAll(3);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse6Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });

        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse4Warnings.txt");
        Shell shell = new Shell("exit 1");
        refJob.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(refJob, Result.FAILURE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);
        refJob.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at complete success builds instead of just looking at the eclipse result.
     * Should return a success result. Uses a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateSuccessResultWithOverAllMustBeSuccessWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse4Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(refJob, publisher -> {
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse6Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });

        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse2Warnings.txt");
        issuesRecorder.setUnstableNewAll(3);
        Shell shell = new Shell("exit 1");
        refJob.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(refJob, Result.FAILURE);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);
        refJob.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    /**
     * Checks if the reference only looks at the eclipse result of a build and not the overall success. Should return an
     * unstable result. Uses a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateUnstableResultWithOverAllMustNotBeSuccessWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse4Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(refJob, publisher -> {
            publisher.setOverallResultMustBeSuccess(false);
            publisher.setEnabledForFailure(true);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse6Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });

        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse2Warnings.txt");
        issuesRecorder.setUnstableNewAll(3);
        Shell shell = new Shell("exit 1");
        refJob.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(refJob, Result.FAILURE);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);
        refJob.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.UNSTABLE);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.UNSTABLE);
    }

    /**
     * Checks if the reference only looks at the eclipse result of a build and not the overall success. Should return an
     * a success result. Uses a different freestyle project for the reference.
     */
    @Test
    public void shouldCreateSuccessResultWithOverAllMustNotBeSuccessWithReferenceBuild()
            throws IOException, InterruptedException {
        FreeStyleProject refJob = createJobWithWorkspaceFile(refName, "eclipse2Warnings.txt");
        IssuesRecorder issuesRecorder = enableWarnings(refJob, publisher -> {
            publisher.setOverallResultMustBeSuccess(false);
            publisher.setEnabledForFailure(true);
            publisher.setUnstableNewAll(3);
        });

        FreeStyleProject project = createJobWithWorkspaceFile(jobName, "eclipse6Warnings.txt");
        enableWarnings(project, publisher -> {
            publisher.setUnstableNewAll(3);
            publisher.setReferenceJobName(refName);
            publisher.setOverallResultMustBeSuccess(true);
            publisher.setEnabledForFailure(true);
        });

        AnalysisResult result;

        result = scheduleBuildAndAssertStatus(refJob, Result.SUCCESS);
        assertThat(result).hasTotalSize(2);
        assertThat(result).hasOverallResult(Result.SUCCESS);

        refJob.getSomeWorkspace().deleteContents();
        copyFilesToWorkspace(refJob, "eclipse4Warnings.txt");

        Shell shell = new Shell("exit 1");
        refJob.getBuildersList().add(shell);

        result = scheduleBuildAndAssertStatus(refJob, Result.FAILURE);
        assertThat(result).hasTotalSize(4);
        assertThat(result).hasOverallResult(Result.SUCCESS);
        refJob.getBuildersList().remove(shell);

        result = scheduleBuildAndAssertStatus(project, Result.SUCCESS);
        assertThat(result).hasTotalSize(6);
        assertThat(result).hasOverallResult(Result.SUCCESS);
    }

    // Copied from IssuesRecorderITest

    /**
     * Creates a new {@link FreeStyleProject freestyle job}. The job will get a generated name.
     *
     * @return the created job
     */
    private FreeStyleProject createJob(String name) {
        try {
            return j.createFreeStyleProject(name);
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
    private FreeStyleProject createJobWithWorkspaceFile(String name, final String... fileNames) {
        FreeStyleProject job = createJob(name);
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
}