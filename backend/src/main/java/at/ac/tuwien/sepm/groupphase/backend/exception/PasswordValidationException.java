package at.ac.tuwien.sepm.groupphase.backend.exception;

public class PasswordValidationException extends RuntimeException{
    public PasswordValidationException() {
    }

    public PasswordValidationException(String message) {
        super(message);
    }

    public PasswordValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordValidationException(Exception e) {
        super(e);
    }
}
