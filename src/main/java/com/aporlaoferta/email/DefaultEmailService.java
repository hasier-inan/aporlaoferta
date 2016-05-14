package com.aporlaoferta.email;

import com.aporlaoferta.dao.EmailTemplateDAO;
import com.aporlaoferta.model.Email;
import com.aporlaoferta.model.EmailTemplate;
import com.aporlaoferta.model.ServerValue;
import com.aporlaoferta.model.TheDefaultOffer;
import com.aporlaoferta.model.TheUser;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:45
 */
@Service
public class DefaultEmailService implements EmailService {

    private static final String ACCOUNT_CONFIRMATION_EMAIL = "AccountConfirmation";
    private static final String PASSWORD_RESET_EMAIL = "PasswordReset";

    private static final String USER_NICKNAME_FIELD = "nickname";
    private static final String USER_AVATAR_IMG_FIELD = "avatarsrc";
    private static final String USER_UUID_FIELD = "userid";
    private static final String SERVER = "server";

    @Autowired
    private EmailSendingService mailSenderService;

    @Autowired
    private VelocityEngine velocity;

    @Autowired
    private ServerValue serverValue;

    @Autowired
    private EmailTemplateDAO emailTemplateDAO;

    public DefaultEmailService() {
    }

    @Override
    public void sendAccountConfirmationEmail(TheUser theUser) throws EmailSendingException {
        sendEmailBasedOnTemplate(theUser, ACCOUNT_CONFIRMATION_EMAIL);
    }

    @Override
    public void sendPasswordForgotten(TheUser theUser) throws EmailSendingException {
        sendEmailBasedOnTemplate(theUser, PASSWORD_RESET_EMAIL);
    }

    public void sendEmailBasedOnTemplate(TheUser theUser, String template) throws EmailSendingException {
        Map<String, String> model = buildEmailModelMap(theUser);
        populateTemplateAndSend(template, model, theUser.getUserEmail());
    }

    private Map<String, String> buildEmailModelMap(TheUser theUser) {
        Map<String, String> model = new HashMap<>();
        populateModelWithEmailContent(theUser, model);
        return model;
    }

    private void populateModelWithEmailContent(TheUser theUser, Map<String, String> model) {
        model.put(USER_NICKNAME_FIELD, theUser.getUserNickname());
        model.put(USER_AVATAR_IMG_FIELD, getUserAvatar(theUser));
        model.put(USER_UUID_FIELD, theUser.getUuid());
        model.put(SERVER, this.serverValue.getValue());
    }

    private String getUserAvatar(TheUser theUser) {
        if(TheDefaultOffer.AVATAR_IMAGE_URL.getCode().equals(theUser.getUserAvatar())){
            return TheDefaultOffer.AVATAR_EXTERNAL_IMAGE_URL.getCode();
        }
        return theUser.getUserAvatar();
    }

    private void populateTemplateAndSend(String templateName, Map<String, String> model, String toAddress) throws EmailSendingException {
        Email email = populateTemplate(templateName, model, toAddress);
        this.mailSenderService.sendEmail(email);
    }

    private Email populateTemplate(String templateName, Map<String, String> model, String toAddress) throws EmailSendingException {
        try {
            EmailTemplate template = this.emailTemplateDAO.findByName(templateName);
            Assert.notNull(template, "templateName: " + templateName);
            return createEmailFromVelocityTemplate(model, toAddress, template);
        }
        catch (IllegalArgumentException | ParseErrorException | MethodInvocationException | ResourceNotFoundException e) {
            String message = String.format("Could not populate mail template with templateName %s. Email address: %s",
                    templateName, toAddress);
            throw new EmailSendingException(message, e);
        }
    }

    private Email createEmailFromVelocityTemplate(Map<String, String> model, String toAddress, EmailTemplate template) {
        Writer bodyWriter = new StringWriter();
        Writer subjectWriter = new StringWriter();
        Context ctx = new VelocityContext(model);
        this.velocity.evaluate(ctx, bodyWriter, getClass().getName(), template.getContent());
        this.velocity.evaluate(ctx, subjectWriter, getClass().getName(), template.getSubject());
        String content = bodyWriter.toString();
        String subject = subjectWriter.toString();
        return new Email(toAddress, subject, content);
    }

    public void setMailSenderService(EmailSendingService mailSenderService) {
        this.mailSenderService = mailSenderService;
    }

    public void setVelocity(VelocityEngine velocity) {
        this.velocity = velocity;
    }

    public void setServerValue(ServerValue server) {
        this.serverValue = server;
    }

    public void setEmailTemplateDAO(EmailTemplateDAO emailTemplateDAO) {
        this.emailTemplateDAO = emailTemplateDAO;
    }
}
