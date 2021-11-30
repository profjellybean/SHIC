package at.ac.tuwien.sepm.groupphase.backend.exception;

public class UsernameTakenException extends RuntimeException{
    public  UsernameTakenException() {
    }

    public UsernameTakenException(String message) {
        super(message);
    }

    public UsernameTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameTakenException(Exception e) {
        super(e);
    }
}
