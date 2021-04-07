package es.upo.witzl.proyectotfg.projects.error;

public final class SubjectAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public SubjectAlreadyExistsException() {
        super();
    }

    public SubjectAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SubjectAlreadyExistsException(final String message) {
        super(message);
    }

    public SubjectAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
