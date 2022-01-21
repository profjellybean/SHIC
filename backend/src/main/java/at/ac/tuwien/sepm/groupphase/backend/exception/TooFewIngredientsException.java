package at.ac.tuwien.sepm.groupphase.backend.exception;

public class TooFewIngredientsException extends RuntimeException {
    public TooFewIngredientsException() {
    }

    public TooFewIngredientsException(String message) {
        super(message);
    }

    public TooFewIngredientsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooFewIngredientsException(Exception e) {
        super(e);
    }
}
