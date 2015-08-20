package com.aporlaoferta.data;

import com.aporlaoferta.model.EmailTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class EmailTemplateBuilder {

    private Long id;
    private String name;
    private String subject;
    private String content;

    public static EmailTemplateBuilder anEmailTemplate() {
        return new EmailTemplateBuilder();
    }

    public EmailTemplateBuilder() {
        super();
    }

    public EmailTemplateBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public EmailTemplateBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailTemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EmailTemplateBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public EmailTemplate build() {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.setContent(this.content);
        emailTemplate.setSubject(this.subject);
        emailTemplate.setName(this.name);
        emailTemplate.setId(this.id);
        return emailTemplate;
    }
}
