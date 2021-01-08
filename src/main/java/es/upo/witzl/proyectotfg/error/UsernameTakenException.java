package es.upo.witzl.proyectotfg.error;

public class UsernameTakenException extends RuntimeException{

    private static final long serialVersionUID = 5861310537366287163L;

    public UsernameTakenException() {
        super();
    }

    public UsernameTakenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UsernameTakenException(final String message) {
        super(message);
    }

    public UsernameTakenException(final Throwable cause) {
        super(cause);
    }
}
