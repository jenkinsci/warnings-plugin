package hudson.plugins.warnings.parser;

import hudson.model.Hudson;
import hudson.plugins.analysis.util.EncodingValidator;
import hudson.plugins.analysis.util.NullLogger;
import hudson.plugins.analysis.util.PluginLogger;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.warnings.GroovyParser;
import hudson.plugins.warnings.WarningsDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Registry for the active parsers in this plug-in.
 *
 * @author Ulli Hafner
 */
// CHECKSTYLE:COUPLING-OFF
public class ParserRegistry {
    private final List<WarningsParser> parsers;
    private final Charset defaultCharset;
    private final Set<Pattern> includePatterns = Sets.newHashSet();
    private final Set<Pattern> excludePatterns = Sets.newHashSet();

    /**
     * Returns all warning parsers registered by the extension point.
     *
     * @return the extension list
     */
    private static List<WarningsParser> all() {
        Hudson instance = Hudson.getInstance();
        if (instance == null) {
            return Lists.newArrayList();
        }
        return instance.getExtensionList(WarningsParser.class);
    }

    /**
     * Returns all available parser names.
     *
     * @return all available parser names
     */
    public static List<String> getAvailableParsers() {
        ArrayList<String> sortedParsers = new ArrayList<String>(getAllParserNames());
        Collections.sort(sortedParsers);
        return Collections.unmodifiableList(sortedParsers);
    }

    private static Set<String> getAllParserNames() {
        Set<String> parsers = new HashSet<String>();
        for (WarningsParser parser : getAllParsers()) {
            parsers.add(parser.getName());
        }
        return parsers;
    }

    /**
     * Returns a list of parsers that match the specified names. Note that the
     * mapping of names to parsers is one to many.
     *
     * @param parserNames
     *            the parser names
     * @return a list of parsers, might be modified by the receiver
     */
    public static List<WarningsParser> getParsers(final Collection<String> parserNames) {
        List<WarningsParser> actualParsers = new ArrayList<WarningsParser>();
        for (String name : parserNames) {
            for (WarningsParser warningsParser : getAllParsers()) {
                if (warningsParser.getName().equals(name)) {
                    actualParsers.add(warningsParser);
                }
            }
        }
        return actualParsers;
    }

    /**
     * Returns all parser names that identify at least one existing parser. The
     * returned list is sorted alphabetically.
     *
     * @param parserNames
     *            the names to filter
     * @return the filtered set, containing only valid names
     */
    public static List<String> filterExistingParserNames(final Set<String> parserNames) {
        List<String> validNames = Lists.newArrayList();

        Set<String> allParsers = getAllParserNames();
        for (String name : parserNames) {
            if (allParsers.contains(name)) {
                validNames.add(name);
            }
        }
        Collections.sort(validNames);

        return validNames;
    }

    /**
     * Returns all available parsers.
     *
     * @return all available parsers
     */
    private static List<WarningsParser> getAllParsers() {
        List<WarningsParser> parsers = new ArrayList<WarningsParser>();
        parsers.add(new JavacParser());
        parsers.add(new AntJavacParser());
        parsers.add(new JavaDocParser());
        parsers.add(new EclipseParser());
        parsers.add(new MsBuildParser());
        parsers.add(new GccParser());
        parsers.add(new Gcc4CompilerParser());
        parsers.add(new Gcc4LinkerParser());
        parsers.add(new InvalidsParser());
        parsers.add(new SunCParser());
        parsers.add(new GnatParser());
        parsers.add(new ErlcParser());
        parsers.add(new IntelCParser());
        parsers.add(new IarParser());
        MsBuildParser pclintParser = new MsBuildParser();
        pclintParser.setName("PC-Lint");
        parsers.add(pclintParser);
        parsers.add(new BuckminsterParser());
        parsers.add(new TiCcsParser());
        parsers.add(new AcuCobolParser());
        parsers.add(new FlexSDKParser());
        parsers.add(new PhpParser());
        parsers.add(new CoolfluxChessccParser());
        parsers.add(new P4Parser());
        parsers.add(new RobocopyParser());
        parsers.add(new DoxygenParser());
        parsers.add(new TnsdlParser());
        parsers.add(new GhsMultiParser());
        parsers.add(new ArmccCompilerParser());
        parsers.add(new YuiCompressorParser());


        Iterable<GroovyParser> parserDescriptions = getDynamicParserDescriptions();
        parsers.addAll(getDynamicParsers(parserDescriptions));
        parsers.addAll(all());

        return ImmutableList.copyOf(parsers);
    }

    private static Iterable<GroovyParser> getDynamicParserDescriptions() {
        Hudson instance = Hudson.getInstance();
        if (instance != null) {
            WarningsDescriptor descriptor = instance.getDescriptorByType(WarningsDescriptor.class);
            if (descriptor != null) {
                return descriptor.getParsers();
            }
        }
        return Collections.emptyList();
    }

    static List<WarningsParser> getDynamicParsers(final Iterable<GroovyParser> parserDescriptions) {
        List<WarningsParser> parsers = new ArrayList<WarningsParser>();
        for (GroovyParser description : parserDescriptions) {
            if (description.isValid()) {
                WarningsParser parser;
                if (description.hasMultiLineSupport()) {
                    parser = new DynamicDocumentParser(description.getName(), description.getRegexp(), description.getScript());
                }
                else {
                    parser = new DynamicParser(description.getName(), description.getRegexp(), description.getScript());
                }
                parsers.add(parser);
            }
        }
        return parsers;
    }

    /**
     * Creates a new instance of <code>ParserRegistry</code>.
     *
     * @param parsers
     *            the parsers to use when scanning a file
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     */
    public ParserRegistry(final List<WarningsParser> parsers, final String defaultEncoding) {
        this(parsers, defaultEncoding, StringUtils.EMPTY, StringUtils.EMPTY);
    }

    /**
     * Creates a new instance of <code>ParserRegistry</code>.
     *
     * @param parsers
     *            the parsers to use when scanning a file
     * @param includePattern
     *            Ant file-set pattern of files to include in report,
     *            <code>null</code> or an empty string do not filter the output
     * @param excludePattern
     *            Ant file-set pattern of files to exclude from report,
     *            <code>null</code> or an empty string do not filter the output
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     */
    public ParserRegistry(final List<? extends WarningsParser> parsers, final String defaultEncoding,
            final String includePattern, final String excludePattern) {
        defaultCharset = EncodingValidator.defaultCharset(defaultEncoding);
        this.parsers = new ArrayList<WarningsParser>(parsers);
        if (this.parsers.isEmpty()) {
            this.parsers.addAll(getAllParsers());
        }
        addPatterns(includePatterns, includePattern);
        addPatterns(excludePatterns, excludePattern);
    }

    private void addPatterns(final Set<Pattern> patterns, final String pattern) {
        if (StringUtils.isNotBlank(pattern)) {
            String[] splitted = StringUtils.split(pattern, ',');
            for (String singlePattern : splitted) {
                String trimmed = StringUtils.trim(singlePattern);
                String directoriesReplaced = StringUtils.replace(trimmed, "**", "*"); // NOCHECKSTYLE
                patterns.add(Pattern.compile(StringUtils.replace(directoriesReplaced, "*", ".*"))); // NOCHECKSTYLE
            }
        }
    }

    /**
     * Iterates over the available parsers and parses the specified file with each parser.
     * Returns all found warnings.
     *
     * @param file the input stream
     * @return all found warnings
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Collection<FileAnnotation> parse(final File file) throws IOException {
        return parse(file, new NullLogger());
    }

    /**
     * Iterates over the available parsers and parses the specified file with
     * each parser. Returns all found warnings.
     *
     * @param file
     *            the input stream
     * @param logger
     *            the logger to write to
     * @return all found warnings
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public Collection<FileAnnotation> parse(final File file, final PluginLogger logger) throws IOException {
        Set<FileAnnotation> allAnnotations = Sets.newHashSet();
        for (WarningsParser parser : parsers) {
            Reader input = null;
            try {
                input = createReader(file);
                Collection<FileAnnotation> warnings = parser.parse(input);
                logger.log(String.format("%s : Found %d warnings.", parser.getName(), warnings.size()));
                allAnnotations.addAll(warnings);
            }
            finally {
                IOUtils.closeQuietly(input);
            }
        }
        return applyExcludeFilter(allAnnotations);
    }

    /**
     * Iterates over the available parsers and parses the specified file with each parser.
     * Returns all found warnings.
     *
     * @param file the input stream
     * @return all found warnings
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public Set<FileAnnotation> parse(final InputStream file) throws IOException {
        try {
            Set<FileAnnotation> allAnnotations = Sets.newHashSet();
            for (WarningsParser parser : parsers) {
                allAnnotations.addAll(parser.parse(createReader(file)));
            }
            return applyExcludeFilter(allAnnotations);
        }
        finally {
            IOUtils.closeQuietly(file);
        }
    }

    /**
     * Applies the exclude filter to the found annotations.
     *
     * @param allAnnotations
     *            all annotations
     * @return the filtered annotations if there is a filter defined
     */
    private Set<FileAnnotation> applyExcludeFilter(final Set<FileAnnotation> allAnnotations) {
        Set<FileAnnotation> includedAnnotations;
        if (includePatterns.isEmpty()) {
            includedAnnotations = allAnnotations;
        }
        else {
            includedAnnotations = Sets.newHashSet();
            for (FileAnnotation annotation : allAnnotations) {
                for (Pattern include : includePatterns) {
                    if (include.matcher(annotation.getFileName()).matches()) {
                        includedAnnotations.add(annotation);
                    }
                }
            }
        }
        if (excludePatterns.isEmpty()) {
            return includedAnnotations;
        }
        else {
            Set<FileAnnotation> excludedAnnotations = Sets.newHashSet(includedAnnotations);
            for (FileAnnotation annotation : includedAnnotations) {
                for (Pattern exclude : excludePatterns) {
                    if (exclude.matcher(annotation.getFileName()).matches()) {
                        excludedAnnotations.remove(annotation);
                    }
                }
            }
            return excludedAnnotations;
        }
    }

    /**
     * Creates a reader from the specified file. Uses the defined character set to
     * read the content of the input stream.
     *
     * @param file the file
     * @return the reader
     * @throws FileNotFoundException if the file does not exist
     */
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("OBL")
    protected Reader createReader(final File file) throws FileNotFoundException {
        return createReader(new FileInputStream(file));
    }

    /**
     * Creates a reader from the specified input stream. Uses the defined character set to
     * read the content of the input stream.
     *
     * @param inputStream the input stream
     * @return the reader
     */
    protected Reader createReader(final InputStream inputStream) {
        return new InputStreamReader(inputStream, defaultCharset);
    }
}

