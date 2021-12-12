package at.ac.tuwien.sepm.groupphase.backend.exception;

public class PasswordTooShortException extends RuntimeException{
    public PasswordTooShortException() {
    }

    public PasswordTooShortException(String message) {
        super(message);
    }

    public PasswordTooShortException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordTooShortException(Exception e) {
        super(e);
    }
}
