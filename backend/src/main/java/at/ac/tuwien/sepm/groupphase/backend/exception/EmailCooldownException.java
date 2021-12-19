package at.ac.tuwien.sepm.groupphase.backend.exception;


public class EmailCooldownException extends RuntimeException {

    public EmailCooldownException() {
    }

    public EmailCooldownException(String message) {
        super(message);
    }

    public EmailCooldownException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailCooldownException(Exception e) {
        super(e);
    }
}
