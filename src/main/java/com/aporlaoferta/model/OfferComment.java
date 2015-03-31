package com.aporlaoferta.model;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TCM_USER", nullable = false)
    private TheUser commentOwner;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TCM_OFFER", nullable = false)
    private TheOffer commentsOffer;

    @Column(name = "TCM_QUOTED_COMMENT", nullable = true)
    private OfferComment commentsQuotedComment;

    @Column(name = "TCM_TEXT", nullable = true, length = 1000)
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

    public OfferComment getCommentsQuotedComment() {
        return commentsQuotedComment;
    }

    public void setCommentsQuotedComment(OfferComment commentsQuotedComment) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferComment)) return false;

        OfferComment that = (OfferComment) o;

        if (commentCreationDate != null ? !commentCreationDate.equals(that.commentCreationDate)
                : that.commentCreationDate != null)
            return false;
        if (commentsOffer != null ? !commentsOffer.equals(that.commentsOffer) : that.commentsOffer != null)
            return false;
        if (commentsQuotedComment != null ? !commentsQuotedComment.equals(that.commentsQuotedComment)
                : that.commentsQuotedComment != null)
            return false;
        if (commentOwner != null ? !commentOwner.equals(that.commentOwner) : that.commentOwner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = commentCreationDate != null ? commentCreationDate.hashCode() : 0;
        result = 31 * result + (commentOwner != null ? commentOwner.hashCode() : 0);
        result = 31 * result + (commentsOffer != null ? commentsOffer.hashCode() : 0);
        result = 31 * result + (commentsQuotedComment != null ? commentsQuotedComment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OfferComment{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", commentCreationDate=").append(commentCreationDate);
        sb.append(", commentOwner=").append(commentOwner);
        sb.append(", commentsOffer=").append(commentsOffer);
        sb.append(", commentsQuotedComment=").append(commentsQuotedComment);
        sb.append('}');
        return sb.toString();
    }
}
