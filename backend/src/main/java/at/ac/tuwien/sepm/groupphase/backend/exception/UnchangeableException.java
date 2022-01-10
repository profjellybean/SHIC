package at.ac.tuwien.sepm.groupphase.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class UnchangeableException extends AuthenticationException {

    public UnchangeableException(String message) {
        super(message);
    }


    public UnchangeableException(HttpStatus conflict, String message) {
        super(message);
    }
}
