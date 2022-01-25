package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.Email;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;


@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String CONFIRMATION_URL = "http://localhost:4200/#/confirm/?token=";
    private static final String CONFIRMATIONNEW_URL = "http://localhost:4200/#/confirmNew/?token=";
    @Autowired
    private JavaMailSender emailSender;


    private void sendEmail(Email mail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mail.getMailFrom());
        message.setTo(mail.getMailTo());
        message.setSubject(mail.getMailSubject());
        message.setText(mail.getMailContent());

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            mimeMessage.setContent(mail.getMailContent(), "text/html");
            helper.setTo(mail.getMailTo());
            helper.setSubject(mail.getMailSubject());
            helper.setFrom(mail.getMailContent());
        } catch (MessagingException e) {
            LOGGER.error(e.getMessage());
        }

        emailSender.send(mimeMessage);

    }

    @Async
    @Override
    public void sendEmailChangeConfirmation(String to, String username, Long confirmationToken) {

        String confirmationTokenEncrypted = Base64.encodeBase64String((username + ":" + confirmationToken).getBytes(StandardCharsets.UTF_8));

        Email registrationMail = new Email();
        registrationMail.setMailFrom("shicregistrator@gmail.com");
        registrationMail.setMailTo(to);
        registrationMail.setMailSubject("SHIC Confirmation");
        registrationMail.setMailContent("<html><body>Hello " + username + "!<h2>Click this link to confirm your new email: </h2>      <h3><a href=\"" + CONFIRMATIONNEW_URL + confirmationTokenEncrypted + "\"> Confirm </a></h3></b></body></html>");

        try {
            sendEmail(registrationMail);
        } catch (MailSendException e) {
            LOGGER.error(e.getMessage());
        }


    }

    @Async
    @Override
    public void sendEmailConfirmation(String to, String username, Long confirmationToken) {

        String confirmationTokenEncrypted = Base64.encodeBase64String((username + ":" + confirmationToken).getBytes(StandardCharsets.UTF_8));

        Email registrationMail = new Email();
        registrationMail.setMailFrom("shicregistrator@gmail.com");
        registrationMail.setMailTo(to);
        registrationMail.setMailSubject("SHIC Confirmation");
        registrationMail.setMailContent("<html><body>Hello " + username + "!<h2>Click this link to complete the registration process: </h2>      <h3><a href=\"" + CONFIRMATION_URL + confirmationTokenEncrypted + "\"> Confirm </a></h3></b></body></html>");

        try {
            sendEmail(registrationMail);
        } catch (MailSendException e) {
            LOGGER.error(e.getMessage());
        }


    }
}