package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.Email;

public interface EmailService {

    void sendEmailConfirmation(String to, String username, Long confirmationToken);

    void sendEmailChangeConfirmation(String to, String username, Long confirmationToken);
}
