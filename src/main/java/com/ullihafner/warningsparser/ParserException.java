package com.ullihafner.warningsparser;

import java.io.IOException;

/**
 * Indicates an exception during parsing
 */
public class ParserException extends IOException {
    private static final long serialVersionUID = -2943620669642726532L;

    private Throwable cause;

    public ParserException() {
        super();
    }

    public ParserException(final String message, final Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public ParserException(final String message) {
        super(message);
    }

    public ParserException(final Throwable cause) {
        super();
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
