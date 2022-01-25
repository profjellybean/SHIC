package at.ac.tuwien.sepm.groupphase.backend.service;

public interface EmailService {

    void sendEmailConfirmation(String to, String username, Long confirmationToken);

    void sendEmailChangeConfirmation(String to, String username, Long confirmationToken);
}
