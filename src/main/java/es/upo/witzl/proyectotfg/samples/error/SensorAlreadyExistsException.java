package es.upo.witzl.proyectotfg.samples.error;

public final class SensorAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;

    public SensorAlreadyExistsException() {
        super();
    }

    public SensorAlreadyExistsException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SensorAlreadyExistsException(final String message) {
        super(message);
    }

    public SensorAlreadyExistsException(final Throwable cause) {
        super(cause);
    }
}
