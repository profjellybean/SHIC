package at.ac.tuwien.sepm.groupphase.backend.exception;

public class EmailConfirmationException extends RuntimeException {
    public EmailConfirmationException() {
    }

    public EmailConfirmationException(String message) {
        super(message);
    }

    public EmailConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailConfirmationException(Exception e) {
        super(e);
    }

}
