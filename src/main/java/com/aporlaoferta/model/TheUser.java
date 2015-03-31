package com.aporlaoferta.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notNull;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 14/01/2015
 * Time: 17:58
 */
@Entity
@Table(name = "thatuser")
@SequenceGenerator(name = "GEN_THATUSER", sequenceName = "SEQ_THATUSER")
public class TheUser implements Serializable {

    private static final long serialVersionUID = 3178810385898694540L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TU_ID")
    private Long id;

    @Version
    @Column(name = "TU_VERSION_ID")
    private Long version;

    @Column(name = "TU_NICKNAME", nullable = false)
    private String userNickname;

    @Column(name = "TU_PWD_LOCKED", nullable = false, length = 60)
    private String userPassword;

    @Column(name = "TU_ENABLED", nullable = false)
    private Boolean enabled;

    @Column(name = "TU_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "TU_AVATAR", nullable = true)
    private String userAvatar;

    @OneToMany(mappedBy = "offerUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TheOffer> userOffers = new HashSet<>();

    @OneToMany(mappedBy = "commentOwner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OfferComment> userComments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Set<TheOffer> getUserOffers() {
        return userOffers;
    }

    public Set<OfferComment> getUserComments() {
        return userComments;
    }

    public void setUserOffers(Set<TheOffer> userOffers) {
        this.userOffers = userOffers;
    }

    public void addOffer(TheOffer theOffer) {
        notNull(theOffer, "Attempting to add null offer object to user.");
        theOffer.setOfferUser(this);
        if (this.userOffers == null) {
            this.userOffers = new HashSet<>();
        }
        this.userOffers.add(theOffer);
    }

    public void setUserComments(Set<OfferComment> userComments) {
        this.userComments = userComments;
    }

    public void addComment(OfferComment offerComment) {
        notNull(offerComment, "Attempting to add null comment object to user.");
        offerComment.setCommentOwner(this);
        if (this.userComments == null) {
            this.userComments = new HashSet<>();
        }
        this.userComments.add(offerComment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TheUser)) return false;

        TheUser theUser = (TheUser) o;

        if (enabled != null ? !enabled.equals(theUser.enabled) : theUser.enabled != null) return false;
        if (id != null ? !id.equals(theUser.id) : theUser.id != null) return false;
        if (userAvatar != null ? !userAvatar.equals(theUser.userAvatar) : theUser.userAvatar != null) return false;
        if (userEmail != null ? !userEmail.equals(theUser.userEmail) : theUser.userEmail != null) return false;
        if (userNickname != null ? !userNickname.equals(theUser.userNickname) : theUser.userNickname != null)
            return false;
        if (userPassword != null ? !userPassword.equals(theUser.userPassword) : theUser.userPassword != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userNickname != null ? userNickname.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userAvatar != null ? userAvatar.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TheUser{");
        sb.append("id=").append(id);
        sb.append(", version=").append(version);
        sb.append(", userNickname='").append(userNickname).append('\'');
        sb.append(", userPassword='").append(userPassword).append('\'');
        sb.append(", enabled=").append(enabled);
        sb.append(", userEmail='").append(userEmail).append('\'');
        sb.append(", userAvatar='").append(userAvatar).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
