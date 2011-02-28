// CHECKSTYLE:OFF

package hudson.plugins.warnings;

import org.jvnet.localizer.Localizable;
import org.jvnet.localizer.ResourceBundleHolder;

@SuppressWarnings({
    "",
    "PMD"
})
public class Messages {

    private final static ResourceBundleHolder holder = ResourceBundleHolder.get(Messages.class);

    /**
     *  1 warning
     * 
     */
    public static String Warnings_ResultAction_OneWarning() {
        return holder.format("Warnings.ResultAction.OneWarning");
    }

    /**
     *  1 warning
     * 
     */
    public static Localizable _Warnings_ResultAction_OneWarning() {
        return new Localizable(holder, "Warnings.ResultAction.OneWarning");
    }

    /**
     * message: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_message(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.message", arg1);
    }

    /**
     * message: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_message(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.message", arg1);
    }

    /**
     * Compiler warnings trend graph (new vs. fixed)
     * 
     */
    public static String Portlet_WarningsNewVsFixedGraph() {
        return holder.format("Portlet.WarningsNewVsFixedGraph");
    }

    /**
     * Compiler warnings trend graph (new vs. fixed)
     * 
     */
    public static Localizable _Portlet_WarningsNewVsFixedGraph() {
        return new Localizable(holder, "Portlet.WarningsNewVsFixedGraph");
    }

    /**
     * {0} new warnings
     * 
     */
    public static String Warnings_ResultAction_MultipleNewWarnings(Object arg1) {
        return holder.format("Warnings.ResultAction.MultipleNewWarnings", arg1);
    }

    /**
     * {0} new warnings
     * 
     */
    public static Localizable _Warnings_ResultAction_MultipleNewWarnings(Object arg1) {
        return new Localizable(holder, "Warnings.ResultAction.MultipleNewWarnings", arg1);
    }

    /**
     * Compiler warnings per project
     * 
     */
    public static String Portlet_WarningsTable() {
        return holder.format("Portlet.WarningsTable");
    }

    /**
     * Compiler warnings per project
     * 
     */
    public static Localizable _Portlet_WarningsTable() {
        return new Localizable(holder, "Portlet.WarningsTable");
    }

    /**
     * Compiler Warnings: {0} warnings found.
     * 
     */
    public static String Warnings_ResultAction_HealthReportMultipleItem(Object arg1) {
        return holder.format("Warnings.ResultAction.HealthReportMultipleItem", arg1);
    }

    /**
     * Compiler Warnings: {0} warnings found.
     * 
     */
    public static Localizable _Warnings_ResultAction_HealthReportMultipleItem(Object arg1) {
        return new Localizable(holder, "Warnings.ResultAction.HealthReportMultipleItem", arg1);
    }

    /**
     * category:  {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_category(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.category", arg1);
    }

    /**
     * category:  {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_category(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.category", arg1);
    }

    /**
     * Scan for compiler warnings
     * 
     */
    public static String Warnings_Publisher_Name() {
        return holder.format("Warnings.Publisher.Name");
    }

    /**
     * Scan for compiler warnings
     * 
     */
    public static Localizable _Warnings_Publisher_Name() {
        return new Localizable(holder, "Warnings.Publisher.Name");
    }

    /**
     * Compiler Warnings Trend
     * 
     */
    public static String Warnings_Trend_Name() {
        return holder.format("Warnings.Trend.Name");
    }

    /**
     * Compiler Warnings Trend
     * 
     */
    public static Localizable _Warnings_Trend_Name() {
        return new Localizable(holder, "Warnings.Trend.Name");
    }

    /**
     * Regular expression must not be empty.
     * 
     */
    public static String Warnings_GroovyParser_Error_Regexp_isEmpty() {
        return holder.format("Warnings.GroovyParser.Error.Regexp.isEmpty");
    }

    /**
     * Regular expression must not be empty.
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Regexp_isEmpty() {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Regexp.isEmpty");
    }

    /**
     * Compiler Warnings: 1 warning found.
     * 
     */
    public static String Warnings_ResultAction_HealthReportSingleItem() {
        return holder.format("Warnings.ResultAction.HealthReportSingleItem");
    }

    /**
     * Compiler Warnings: 1 warning found.
     * 
     */
    public static Localizable _Warnings_ResultAction_HealthReportSingleItem() {
        return new Localizable(holder, "Warnings.ResultAction.HealthReportSingleItem");
    }

    /**
     * Fixed Compiler Warnings Warnings
     * 
     */
    public static String Warnings_FixedWarnings_Detail_header() {
        return holder.format("Warnings.FixedWarnings.Detail.header");
    }

    /**
     * Fixed Compiler Warnings Warnings
     * 
     */
    public static Localizable _Warnings_FixedWarnings_Detail_header() {
        return new Localizable(holder, "Warnings.FixedWarnings.Detail.header");
    }

    /**
     * An exception occurred during evaluation of the Groovy script: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_exception(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.exception", arg1);
    }

    /**
     * An exception occurred during evaluation of the Groovy script: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_exception(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.exception", arg1);
    }

    /**
     * file name: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_file(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.file", arg1);
    }

    /**
     * file name: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_file(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.file", arg1);
    }

    /**
     * Display name must not be empty.
     * 
     */
    public static String Warnings_GroovyParser_Error_Name_isEmpty() {
        return holder.format("Warnings.GroovyParser.Error.Name.isEmpty");
    }

    /**
     * Display name must not be empty.
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Name_isEmpty() {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Name.isEmpty");
    }

    /**
     * {0} fixed warnings
     * 
     */
    public static String Warnings_ResultAction_MultipleFixedWarnings(Object arg1) {
        return holder.format("Warnings.ResultAction.MultipleFixedWarnings", arg1);
    }

    /**
     * {0} fixed warnings
     * 
     */
    public static Localizable _Warnings_ResultAction_MultipleFixedWarnings(Object arg1) {
        return new Localizable(holder, "Warnings.ResultAction.MultipleFixedWarnings", arg1);
    }

    /**
     * One warning found
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_title() {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.title");
    }

    /**
     * One warning found
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_title() {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.title");
    }

    /**
     * This is not a valid regular expression: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Regexp_invalid(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Regexp.invalid", arg1);
    }

    /**
     * This is not a valid regular expression: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Regexp_invalid(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Regexp.invalid", arg1);
    }

    /**
     * Result of the script is not of type Warning: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_wrongReturnType(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.wrongReturnType", arg1);
    }

    /**
     * Result of the script is not of type Warning: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_wrongReturnType(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.wrongReturnType", arg1);
    }

    /**
     *  1 new warning
     * 
     */
    public static String Warnings_ResultAction_OneNewWarning() {
        return holder.format("Warnings.ResultAction.OneNewWarning");
    }

    /**
     *  1 new warning
     * 
     */
    public static Localizable _Warnings_ResultAction_OneNewWarning() {
        return new Localizable(holder, "Warnings.ResultAction.OneNewWarning");
    }

    /**
     * type: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_type(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.type", arg1);
    }

    /**
     * type: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_type(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.type", arg1);
    }

    /**
     * Groovy script must not be empty.
     * 
     */
    public static String Warnings_GroovyParser_Error_Script_isEmpty() {
        return holder.format("Warnings.GroovyParser.Error.Script.isEmpty");
    }

    /**
     * Groovy script must not be empty.
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Script_isEmpty() {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Script.isEmpty");
    }

    /**
     * Compiler warnings trend graph (priority distribution)
     * 
     */
    public static String Portlet_WarningsPriorityGraph() {
        return holder.format("Portlet.WarningsPriorityGraph");
    }

    /**
     * Compiler warnings trend graph (priority distribution)
     * 
     */
    public static Localizable _Portlet_WarningsPriorityGraph() {
        return new Localizable(holder, "Portlet.WarningsPriorityGraph");
    }

    /**
     * New Compiler Warnings Warnings
     * 
     */
    public static String Warnings_NewWarnings_Detail_header() {
        return holder.format("Warnings.NewWarnings.Detail.header");
    }

    /**
     * New Compiler Warnings Warnings
     * 
     */
    public static Localizable _Warnings_NewWarnings_Detail_header() {
        return new Localizable(holder, "Warnings.NewWarnings.Detail.header");
    }

    /**
     * Compiler Warnings: no warnings found.
     * 
     */
    public static String Warnings_ResultAction_HealthReportNoItem() {
        return holder.format("Warnings.ResultAction.HealthReportNoItem");
    }

    /**
     * Compiler Warnings: no warnings found.
     * 
     */
    public static Localizable _Warnings_ResultAction_HealthReportNoItem() {
        return new Localizable(holder, "Warnings.ResultAction.HealthReportNoItem");
    }

    /**
     * Compiler Warnings
     * 
     */
    public static String Warnings_ProjectAction_Name() {
        return holder.format("Warnings.ProjectAction.Name");
    }

    /**
     * Compiler Warnings
     * 
     */
    public static Localizable _Warnings_ProjectAction_Name() {
        return new Localizable(holder, "Warnings.ProjectAction.Name");
    }

    /**
     * priority: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_priority(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.priority", arg1);
    }

    /**
     * priority: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_priority(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.priority", arg1);
    }

    /**
     * line number: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_ok_line(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Example.ok.line", arg1);
    }

    /**
     * line number: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_ok_line(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.ok.line", arg1);
    }

    /**
     * This is not a valid Groovy script: {0}
     * 
     */
    public static String Warnings_GroovyParser_Error_Script_invalid(Object arg1) {
        return holder.format("Warnings.GroovyParser.Error.Script.invalid", arg1);
    }

    /**
     * This is not a valid Groovy script: {0}
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Script_invalid(Object arg1) {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Script.invalid", arg1);
    }

    /**
     * The regular expression does not match the example text.
     * 
     */
    public static String Warnings_GroovyParser_Error_Example_regexpDoesNotMatch() {
        return holder.format("Warnings.GroovyParser.Error.Example.regexpDoesNotMatch");
    }

    /**
     * The regular expression does not match the example text.
     * 
     */
    public static Localizable _Warnings_GroovyParser_Error_Example_regexpDoesNotMatch() {
        return new Localizable(holder, "Warnings.GroovyParser.Error.Example.regexpDoesNotMatch");
    }

    /**
     * {0} warnings
     * 
     */
    public static String Warnings_ResultAction_MultipleWarnings(Object arg1) {
        return holder.format("Warnings.ResultAction.MultipleWarnings", arg1);
    }

    /**
     * {0} warnings
     * 
     */
    public static Localizable _Warnings_ResultAction_MultipleWarnings(Object arg1) {
        return new Localizable(holder, "Warnings.ResultAction.MultipleWarnings", arg1);
    }

    /**
     *  1 fixed warning
     * 
     */
    public static String Warnings_ResultAction_OneFixedWarning() {
        return holder.format("Warnings.ResultAction.OneFixedWarning");
    }

    /**
     *  1 fixed warning
     * 
     */
    public static Localizable _Warnings_ResultAction_OneFixedWarning() {
        return new Localizable(holder, "Warnings.ResultAction.OneFixedWarning");
    }

}
