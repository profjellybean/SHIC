package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailConfirmationException  extends AuthenticationException {

    public EmailConfirmationException(String message) {
        super(message);
    }

    public EmailConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }


}
