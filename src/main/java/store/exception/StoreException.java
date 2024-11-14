package store.exception;

public class StoreException extends IllegalArgumentException {
    private static final String PREFIX = "[ERROR] ";

    public StoreException(final String message) {
        super(PREFIX + message);
    }
}
