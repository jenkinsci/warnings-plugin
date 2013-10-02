package com.ullihafner.warningsparser;

/**
 * @author Ulli Hafner
 */
public class Warning {

    public static enum Priority {
        LOW, NORMAL, HIGH
    }

    private final String fileName;
    private final int lineStart;
    private final int lineEnd;
    private int columnStart = -1;
    private int columnEnd = -1;
    private final String type;
    private final String category;
    private final String message;
    private final Priority priority;
    private String packageName;
    private String toolTip;
    private String origin;
    private String workspacePath;

    /**
     * Creates a new instance of {@link Warning}.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     */
    public Warning(final String fileName, final int start, final String category, final String message) {
        this(fileName, start, category, message, Priority.NORMAL);
    }

    /**
     * Creates a new instance of <code>Warning</code>.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     */
    public Warning(final String fileName, final int start, final String category, final String message,
            final Priority priority) {
        this(fileName, start, null, category, message, priority);
    }

    /**
     * Creates a new instance of <code>Warning</code>.
     *
     * @param fileName
     *            the name of the file
     * @param start
     *            the first line of the line range
     * @param type
     *            the identifier of the warning type
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     */
    public Warning(final String fileName, final int start, final String type, final String category,
            final String message, final Priority priority) {
        this(fileName, start, start, type, category, message, priority);
    }

    /**
     * Creates a new instance of <code>Warning</code>.
     *
     * @param fileName
     *            the name of the file
     * @param lineStart
     *            the first line of the line range
     * @param lineEnd
     *            the end line of the line range
     * @param type
     *            the identifier of the warning type
     * @param category
     *            the warning category
     * @param message
     *            the message of the warning
     * @param priority
     *            the priority of the warning
     */
    public Warning(final String fileName, final int lineStart, final int lineEnd, final String type,
            final String category, final String message, final Priority priority) {
        this.fileName = fileName;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.type = type;
        this.category = category;
        this.message = message;
        this.priority = priority;
    }

    /**
     * Creates a new instance of {@link Warning}. This warning is a copy of the specified warning with the additional
     * message text (at the specified line).
     *
     * @param copy
     *            the warning to copy
     * @param additionalMessage
     *            the additional message text
     * @param currentLine
     *            the current line
     */
    public Warning(final Warning copy, final String additionalMessage, final int currentLine) {
        this(copy.fileName, copy.lineStart, currentLine, copy.type, copy.category, copy.message + "\n"
                + additionalMessage, copy.priority);
    }

    public int getColumnStart() {
        return columnStart;
    }

    public int getColumnEnd() {
        return columnEnd;
    }

    public void setColumnPosition(final int column) {
        columnStart = columnEnd = column;
    }

    public void setColumnPosition(final int columnStart, final int columnEnd) {
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPrimaryLineNumber() {
        return lineStart;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String packageName) {
        this.packageName = packageName;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(final String toolTip) {
        this.toolTip = toolTip;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(final String origin) {
        this.origin = origin;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(final String workspacePath) {
        this.workspacePath = workspacePath;
    }

}
