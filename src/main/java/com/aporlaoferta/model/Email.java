package com.aporlaoferta.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 15:58
 */
public class Email {
    private final String toAddress;
    private final String subject;
    private final String content;
    private final List<String> attachments;

    public Email(String toAddress, String subject, String content) {
        this.toAddress = toAddress;
        this.subject = subject;
        this.content = content;
        this.attachments = new ArrayList<>();
    }

    public String toAddress() {
        return this.toAddress;
    }

    public String subject() {
        return this.subject;
    }

    public String content() {
        return this.content;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    public void addAttachment(String filePath) {
        if (filePath != null && !filePath.trim().isEmpty()) {
            this.attachments.add(filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Email [toAddress=");
        builder.append(toAddress);
        builder.append(", subject=");
        builder.append(subject);
        builder.append(", content=");
        builder.append(content.length() > 50 ? content.subSequence(0, 50) + "..." : content);
        builder.append("]");
        return builder.toString();
    }
}
