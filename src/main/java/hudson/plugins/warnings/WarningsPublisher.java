package hudson.plugins.warnings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import hudson.Launcher;
import hudson.matrix.MatrixAggregator;
import hudson.matrix.MatrixBuild;

import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

import hudson.plugins.analysis.core.AnnotationsClassifier;
import hudson.plugins.analysis.core.FilesParser;
import hudson.plugins.analysis.core.HealthAwareRecorder;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.util.ModuleDetector;
import hudson.plugins.analysis.util.NullModuleDetector;
import hudson.plugins.analysis.util.PluginLogger;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.warnings.parser.FileWarningsParser;
import hudson.plugins.warnings.parser.ParserRegistry;
import hudson.plugins.warnings.parser.ParsingCanceledException;

/**
 * Publishes the results of the warnings analysis (freestyle project type).
 *
 * @author Ulli Hafner
 */
// CHECKSTYLE:COUPLING-OFF
public class WarningsPublisher extends HealthAwareRecorder {
    private static final String CONSOLE_LOG_ENCODING = "UTF-8";
    private static final String PLUGIN_NAME = "WARNINGS";
    private static final long serialVersionUID = -5936973521277401764L;

    /** Ant file-set pattern of files to include to report. */
    private final String includePattern;
    /** Ant file-set pattern of files to exclude from report. */
    private final String excludePattern;

    /** File pattern and parser configurations. @since 3.19 */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE")
    private List<ParserConfiguration> parserConfigurations = Lists.newArrayList();
    /** Parser configurations of the console. @since 4.6 */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("SE")
    private List<ConsoleParser> consoleParsers = Lists.newArrayList();

    /**
     * Creates a new instance of <code>WarningPublisher</code>.
     *
     * @param healthy
     *            Report health as 100% when the number of annotations is less
     *            than this value
     * @param unHealthy
     *            Report health as 0% when the number of annotations is greater
     *            than this value
     * @param thresholdLimit
     *            determines which warning priorities should be considered when
     *            evaluating the build stability and health
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     * @param useDeltaValues
     *            determines whether the absolute annotations delta or the
     *            actual annotations set difference should be used to evaluate
     *            the build stability
     * @param unstableTotalAll
     *            annotation threshold
     * @param unstableTotalHigh
     *            annotation threshold
     * @param unstableTotalNormal
     *            annotation threshold
     * @param unstableTotalLow
     *            annotation threshold
     * @param unstableNewAll
     *            annotation threshold
     * @param unstableNewHigh
     *            annotation threshold
     * @param unstableNewNormal
     *            annotation threshold
     * @param unstableNewLow
     *            annotation threshold
     * @param failedTotalAll
     *            annotation threshold
     * @param failedTotalHigh
     *            annotation threshold
     * @param failedTotalNormal
     *            annotation threshold
     * @param failedTotalLow
     *            annotation threshold
     * @param failedNewAll
     *            annotation threshold
     * @param failedNewHigh
     *            annotation threshold
     * @param failedNewNormal
     *            annotation threshold
     * @param failedNewLow
     *            annotation threshold
     * @param canRunOnFailed
     *            determines whether the plug-in can run for failed builds, too
     * @param useStableBuildAsReference
     *            determines whether only stable builds should be used as reference builds or not
     * @param canComputeNew
     *            determines whether new warnings should be computed (with
     *            respect to baseline)
     * @param shouldDetectModules
     *            determines whether module names should be derived from Maven
     *            POM or Ant build files
     * @param includePattern
     *            Ant file-set pattern of files to include in report
     * @param excludePattern
     *            Ant file-set pattern of files to exclude from report
     * @param canResolveRelativePaths
     *            determines whether relative paths in warnings should be
     *            resolved using a time expensive operation that scans the whole
     *            workspace for matching files.
     * @param parserConfigurations
     *            the parser configurations to scan files
     * @param consoleParsers
     *            the parsers to scan the console
     */
    // CHECKSTYLE:OFF
    @SuppressWarnings("PMD.ExcessiveParameterList")
    @DataBoundConstructor
    public WarningsPublisher(final String healthy, final String unHealthy, final String thresholdLimit,
            final String defaultEncoding, final boolean useDeltaValues,
            final String unstableTotalAll, final String unstableTotalHigh, final String unstableTotalNormal, final String unstableTotalLow,
            final String unstableNewAll, final String unstableNewHigh, final String unstableNewNormal, final String unstableNewLow,
            final String failedTotalAll, final String failedTotalHigh, final String failedTotalNormal, final String failedTotalLow,
            final String failedNewAll, final String failedNewHigh, final String failedNewNormal, final String failedNewLow,
            final boolean canRunOnFailed, final boolean useStableBuildAsReference, final boolean shouldDetectModules,
            final boolean canComputeNew, final String includePattern, final String excludePattern, final boolean canResolveRelativePaths,
            final List<ParserConfiguration> parserConfigurations, final List<ConsoleParser> consoleParsers) {
        super(healthy, unHealthy, thresholdLimit, defaultEncoding, useDeltaValues,
                unstableTotalAll, unstableTotalHigh, unstableTotalNormal, unstableTotalLow,
                unstableNewAll, unstableNewHigh, unstableNewNormal, unstableNewLow,
                failedTotalAll, failedTotalHigh, failedTotalNormal, failedTotalLow,
                failedNewAll, failedNewHigh, failedNewNormal, failedNewLow,
                canRunOnFailed, useStableBuildAsReference, shouldDetectModules, canComputeNew, canResolveRelativePaths, PLUGIN_NAME);
        this.includePattern = StringUtils.stripToNull(includePattern);
        this.excludePattern = StringUtils.stripToNull(excludePattern);
        if (consoleParsers != null) {
            this.consoleParsers.addAll(consoleParsers);
        }
        if (parserConfigurations != null) {
            this.parserConfigurations.addAll(parserConfigurations);
        }
    }
    // CHECKSTYLE:ON

    /**
     * Returns the names of the configured parsers for the console log.
     *
     * @return the parser names
     */
    public ConsoleParser[] getConsoleParsers() {
        return ConsoleParser.filterExisting(consoleParsers);
    }

    /**
     * Returns the parserConfigurations.
     *
     * @return the parserConfigurations
     */
    public ParserConfiguration[] getParserConfigurations() {
        return ParserConfiguration.filterExisting(parserConfigurations);
    }

    /**
     * Upgrade for release 4.5 or older.
     *
     * @return this
     */
    @Override
    protected Object readResolve() {
        super.readResolve();

        if (consoleParsers == null) {
            consoleParsers = Lists.newArrayList();

            if (isOlderThanRelease318()) {
                upgradeFrom318();
            }

            for (String  parser : consoleLogParsers) {
                consoleParsers.add(new ConsoleParser(parser));
            }
        }

        replaceConsoleParsersWithChangedName();
        replaceFileParsersWithChangedName();

        return this;
    }

    private void replaceConsoleParsersWithChangedName() {
        List<ConsoleParser> updatedConsoleParsers = new ArrayList<ConsoleParser>(consoleParsers);
        for (ConsoleParser parser : consoleParsers) {
            String parserName = parser.getParserName();
            if (ParserRegistry.exists(parserName)) {
                String group = getGroup(parserName);
                if (!group.equals(parserName)) {
                    updatedConsoleParsers.remove(parser);
                    updatedConsoleParsers.add(new ConsoleParser(group));
                }
            }
            consoleParsers = updatedConsoleParsers;
        }
    }

    private void replaceFileParsersWithChangedName() {
        List<ParserConfiguration> updatedFileParsers = new ArrayList<ParserConfiguration>(parserConfigurations);
        for (ParserConfiguration parser : parserConfigurations) {
            String parserName = parser.getParserName();
            if (ParserRegistry.exists(parserName)) {
                String group = getGroup(parserName);
                if (!group.equals(parserName)) {
                    updatedFileParsers.remove(parser);
                    updatedFileParsers.add(new ParserConfiguration(parser.getPattern(), group));
                }
            }
            parserConfigurations = updatedFileParsers;
        }
    }

    private String getGroup(final String parserName) {
        return ParserRegistry.getParser(parserName).getGroup();
    }

    private void upgradeFrom318() {
        consoleLogParsers = Sets.newHashSet();
        parserConfigurations = Lists.newArrayList();

        if (parserNames != null) {
            convertToNewFormat();
        }
    }

    private boolean isOlderThanRelease318() {
        return consoleLogParsers == null || parserConfigurations == null;
    }

    private void convertToNewFormat() {
        if (!ignoreConsole) {
            consoleLogParsers.addAll(parserNames);
        }
        if (StringUtils.isNotBlank(pattern)) {
            for (String parser : parserNames) {
                parserConfigurations.add(new ParserConfiguration(pattern, parser));
            }
        }
    }

    /**
     * Returns the Ant file-set pattern of files to include in report.
     *
     * @return Ant file-set pattern of files to include in report
     */
    public String getIncludePattern() {
        return includePattern;
    }

    /**
     * Returns the Ant file-set pattern of files to exclude from report.
     *
     * @return Ant file-set pattern of files to exclude from report
     */
    public String getExcludePattern() {
        return excludePattern;
    }

    @Override
    public Action getProjectAction(final AbstractProject<?, ?> project) {
        throw new IllegalStateException("Not available since release 4.0.");
    }

    @Override
    public Collection<? extends Action> getProjectActions(final AbstractProject<?, ?> project) {
        List<Action> actions = Lists.newArrayList();
        for (String parserName : getParsers()) {
            actions.add(new WarningsProjectAction(project, parserName));
        }
        return actions;
    }

    private List<String> getParsers() {
        List<String> parsers = Lists.newArrayList();
        for (ConsoleParser configuration : getConsoleParsers()) {
            parsers.add(configuration.getParserName());
        }
        for (ParserConfiguration configuration : getParserConfigurations()) {
            parsers.add(configuration.getParserName());
        }
        return parsers;
    }

    /** {@inheritDoc} */
    @Override
    protected boolean perform(final AbstractBuild<?, ?> build, final Launcher launcher, final PluginLogger logger)
            throws InterruptedException, IOException {
        try {
            if (!hasConsoleParsers() && !hasFileParsers()) {
                throw new IOException("Error: No warning parsers defined in the job configuration.");
            }

            List<ParserResult> fileResults = parseFiles(build, logger);
            List<ParserResult> consoleResults = parseConsoleLog(build, logger);

            ParserResult totals = new ParserResult();
            add(totals, consoleResults);
            add(totals, fileResults);

            if (isThresholdEnabled()) {
                evaluateBuildHealth(build, logger);
            }

            //new ....(totals)
            String allParsers = "totalParsers";
            WarningsBuildHistory history = new WarningsBuildHistory(build, allParsers, useOnlyStableBuildsAsReference());
            WarningsTotalResult result = new WarningsTotalResult(build, history, totals, getDefaultEncoding());
            build.getActions().add(new WarningsTotalResultAction(build, this, result));

            copyFilesWithAnnotationsToBuildFolder(build.getRootDir(), launcher.getChannel(), totals.getAnnotations());

            return true;
        }
        catch (ParsingCanceledException exception) {
            return stopParsing(logger, exception);
        }
        catch (InterruptedException exception) {
            return stopParsing(logger, exception);
        }
    }

    private boolean stopParsing(final PluginLogger logger, final Exception exception) {
        logger.log(exception.getMessage());

        return false;
    }

    private void evaluateBuildHealth(final AbstractBuild<?, ?> build, final PluginLogger logger) {
        for (WarningsResultAction action : build.getActions(WarningsResultAction.class)) {
            WarningsBuildHistory history = new WarningsBuildHistory(build, action.getParser(), useOnlyStableBuildsAsReference());
            AbstractBuild<?, ?> referenceBuild = history.getReferenceBuild();
            if (referenceBuild == null) {
                logger.log("Skipping warning delta computation since no reference build is found");
            }
            else {
                logger.log("Computing warning deltas based on reference build " + referenceBuild.getDisplayName());
                action.getResult().evaluateStatus(getThresholds(), getUseDeltaValues(), canComputeNew(),
                        logger, action.getUrlName());
            }
        }
    }

    private boolean hasFileParsers() {
        return getParserConfigurations().length > 0;
    }

    private boolean hasConsoleParsers() {
        return getConsoleParsers().length > 0;
    }

    private void add(final ParserResult totals, final List<ParserResult> results) {
        for (ParserResult result : results) {
            totals.addProject(result);
        }
    }

    private InterruptedException createInterruptedException() {
        return new InterruptedException("Canceling parsing since build has been aborted.");
    }

    private void returnIfCanceled() throws InterruptedException {
        if (Thread.interrupted()) {
            throw createInterruptedException();
        }
    }

    private List<ParserResult> parseConsoleLog(final AbstractBuild<?, ?> build,
            final PluginLogger logger) throws IOException, InterruptedException {
        List<ParserResult> results = Lists.newArrayList();
        for (ConsoleParser parser : getConsoleParsers()) {
            String parserName = parser.getParserName();
            logger.log("Parsing warnings in console log with parser " + parserName);

            Collection<FileAnnotation> warnings = new ParserRegistry(ParserRegistry.getParsers(parserName),
                    CONSOLE_LOG_ENCODING, getIncludePattern(), getExcludePattern()).parse(build.getLogFile());
            if (!build.getWorkspace().isRemote()) {
                guessModuleNames(build, warnings);
            }
            ParserResult project;
            if (canResolveRelativePaths()) {
                project = new ParserResult(build.getWorkspace());
            }
            else {
                project = new ParserResult();
            }
            project.addAnnotations(warnings);
            results.add(annotate(build, project, parserName));
        }
        return results;
    }

    private void guessModuleNames(final AbstractBuild<?, ?> build, final Collection<FileAnnotation> warnings) {
        String workspace = build.getWorkspace().getRemote();
        ModuleDetector detector = createModuleDetector(workspace);
        for (FileAnnotation annotation : warnings) {
            String module = detector.guessModuleName(annotation.getFileName());
            annotation.setModuleName(module);
        }
    }

    private List<ParserResult> parseFiles(final AbstractBuild<?, ?> build, final PluginLogger logger)
            throws IOException, InterruptedException {
        List<ParserResult> results = Lists.newArrayList();
        for (ParserConfiguration configuration : getParserConfigurations()) {
            String filePattern = configuration.getPattern();
            String parserName = configuration.getParserName();
            logger.log("Parsing warnings in files '" + filePattern + "' with parser " + parserName);

            FilesParser parser = new FilesParser(PLUGIN_NAME, filePattern,
                    new FileWarningsParser(ParserRegistry.getParsers(parserName), getDefaultEncoding(), getIncludePattern(), getExcludePattern()),
                    shouldDetectModules(), isMavenBuild(build), canResolveRelativePaths());
            ParserResult project = build.getWorkspace().act(parser);
            logger.logLines(project.getLogMessages());

            returnIfCanceled();
            results.add(annotate(build, project, configuration.getParserName()));
        }
        return results;
    }

    private ParserResult annotate(final AbstractBuild<?, ?> build, final ParserResult input, final String parserName)
            throws IOException, InterruptedException {
        ParserResult output = build.getWorkspace().act(new AnnotationsClassifier(input, getDefaultEncoding()));
        for (FileAnnotation annotation : output.getAnnotations()) {
            annotation.setPathName(build.getWorkspace().getRemote());
        }
        WarningsBuildHistory history = new WarningsBuildHistory(build, parserName, useOnlyStableBuildsAsReference());
        WarningsResult result = new WarningsResult(build, history, output, getDefaultEncoding(), parserName);
        build.getActions().add(new WarningsResultAction(build, this, result, parserName));

        return output;
    }

    private ModuleDetector createModuleDetector(final String workspace) {
        if (shouldDetectModules()) {
            return new ModuleDetector(new File(workspace));
        }
        else {
            return new NullModuleDetector();
        }
    }

    @Override
    public WarningsDescriptor getDescriptor() {
        return (WarningsDescriptor)super.getDescriptor();
    }

    /** {@inheritDoc} */
    public MatrixAggregator createAggregator(final MatrixBuild build, final Launcher launcher, final BuildListener listener) {
        return new WarningsAnnotationsAggregator(build, launcher, listener, this, getDefaultEncoding(), useOnlyStableBuildsAsReference());
    }

    /** Name of parsers to use for scanning the logs. */
    @SuppressWarnings("PMD")
    private transient Set<String> parserNames;
    /** Determines whether the console should be ignored. */
    @SuppressWarnings("PMD")
    private transient boolean ignoreConsole;
    /** Ant file-set pattern of files to work with. */
    @SuppressWarnings("PMD")
    private transient String pattern;
    /** Parser to scan the console log. @since 3.19 */
    private transient Set<String> consoleLogParsers;
}
