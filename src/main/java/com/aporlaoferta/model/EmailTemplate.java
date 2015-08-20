package com.aporlaoferta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 16:14
 */
@Entity
@Table(name = "thattemplate")
@SequenceGenerator(name = "GEN_THATTEMPLATE", sequenceName = "SEQ_THATTEMPLATE")
public class EmailTemplate implements Serializable {

    private static final long serialVersionUID = 219155720483699002L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EMT_ID")
    private Long id;

    @Column(name = "EMT_NAME", nullable = false)
    private String name;

    @Column(name = "EMT_SUBJECT", nullable = false)
    private String subject;

    @Column(name = "EMT_CONTENT", columnDefinition = "CLOB")
    @Lob
    private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
