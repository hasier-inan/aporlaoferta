package com.aporlaoferta.email;

import com.aporlaoferta.model.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:58
 */
public class DefaultEmailSendingService implements EmailSendingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmailSendingService.class);
    private static final String DEFAULT_ENCODING = "UTF-8";

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public DefaultEmailSendingService(JavaMailSender mailSender, String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendEmail(Email email) throws EmailSendingException {
        MimeMessage mimeMessage = createMimeMessage(email);
        try {
            LOGGER.debug("Attempting to send Message: {}", email);
            this.mailSender.send(mimeMessage);
            LOGGER.debug("sent Message: {}", email);
        } catch (MailException e) {
            String message = String.format("Could not send email '%s' to email address '%s'", email.subject(),
                    email.toAddress());
            throw new EmailSendingException(message, e);
        }
    }

    public MimeMessage createMimeMessage(Email email) throws EmailSendingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, DEFAULT_ENCODING);
            helper.setTo(email.toAddress());
            helper.setFrom(this.fromAddress);
            helper.setSubject(email.subject());
            helper.setSentDate(new Date());
            addHtmlBody(email, mimeMessage, helper);
            return mimeMessage;
        } catch (MessagingException | MailException e) {
            String message = String.format("Could not create mime message '%s' to '%s'", email.subject(),
                    email.toAddress());
            throw new EmailSendingException(message, e);
        }
    }

    private void addHtmlBody(Email email, MimeMessage mimeMessage, MimeMessageHelper helper) throws MessagingException {
        if (email.getAttachments().isEmpty()) {
            addHtmlBodyToHelper(email, helper);
        } else {
            addHtmlBodyToTheMultipart(email, mimeMessage);
        }
    }

    private void addHtmlBodyToTheMultipart(Email email, MimeMessage mimeMessage) throws MessagingException {
        mimeMessage.setContent(attachFile(email.getAttachments(), email.content()));
    }

    private void addHtmlBodyToHelper(Email email, MimeMessageHelper helper) throws MessagingException {
        helper.setText("", email.content());
    }

    private Multipart attachFile(List<String> filePaths, String htmlBody) throws MessagingException {
        Multipart multipart = new MimeMultipart("mixed");
        attachFilesToBodyPart(filePaths, multipart);
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);
        return multipart;
    }

    private void attachFilesToBodyPart(List<String> filePaths, Multipart multipart) {
        for (String fileToBeAttached : filePaths) {
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            File file = new File(fileToBeAttached);
            attachFileIfExists(multipart, fileToBeAttached, messageBodyPart, file);
        }
    }

    private void attachFileIfExists(Multipart multipart, String fileToBeAttached, MimeBodyPart messageBodyPart, File file) {
        if (file.exists()) {
            DataSource source = new FileDataSource(fileToBeAttached);
            try {
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(source.getName());
                multipart.addBodyPart(messageBodyPart);
                LOGGER.debug("Attachment added to the message body part: {}", source.getName());
            } catch (MessagingException e) {
                String message = String.format("Could not attach file: %s", e.getMessage());
                LOGGER.error(message, e);
            }
        } else {
            LOGGER.warn("Can not attach file as it does not exist: {}", fileToBeAttached);
        }
    }
}
