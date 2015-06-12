package com.aporlaoferta.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/01/2015
 * Time: 18:15
 */
@Entity
@Table(name = "thatcomment")
@SequenceGenerator(name = "GEN_THATCOMMENT", sequenceName = "SEQ_THATCOMMENT")
public class OfferComment implements Serializable {

    private static final long serialVersionUID = 1035376396865210972L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TCM_ID")
    private Long id;

    @Version
    @Column(name = "TCM_VERSION_ID")
    private Long version;

    @Column(name = "TCM_CREATION_DATE", nullable = false)
    @CreatedDate
    private Date commentCreationDate;

    @ManyToOne
    @JoinColumn(name = "TCM_USER", nullable = false)
    private TheUser commentOwner;

    @ManyToOne
    @JoinColumn(name = "TCM_OFFER", nullable = false)
    @JsonIgnore
    private TheOffer commentsOffer;

    @Column(name = "TCM_QUOTED_COMMENT", nullable = true)
    private Long commentsQuotedComment;

    @Column(name = "TCM_TEXT", nullable = false)
    private String commentText;

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Date getCommentCreationDate() {
        return commentCreationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCommentCreationDate(Date commentCreationDate) {
        this.commentCreationDate = commentCreationDate;
    }

    public Long getCommentsQuotedComment() {
        return commentsQuotedComment;
    }

    public void setCommentsQuotedComment(Long commentsQuotedComment) {
        this.commentsQuotedComment = commentsQuotedComment;
    }

    public TheUser getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(TheUser commentOwner) {
        this.commentOwner = commentOwner;
    }

    public TheOffer getCommentsOffer() {
        return commentsOffer;
    }

    public void setCommentsOffer(TheOffer commentsOffer) {
        this.commentsOffer = commentsOffer;
    }

}
