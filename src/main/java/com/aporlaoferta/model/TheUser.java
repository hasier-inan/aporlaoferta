package com.aporlaoferta.model;

import com.aporlaoferta.utils.CommentComparator;
import com.aporlaoferta.utils.OfferComparator;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "TU_PWD_LOCKED", nullable = false, length = 60 )
    private String userPassword;

    @Column(name = "TU_ENABLED", nullable = false)
    private Boolean enabled;

    @Column(name = "TU_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "TU_AVATAR", nullable = true)
    private String userAvatar;

    @OneToMany(mappedBy = "offerUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TheOffer> userOffers = new HashSet<>();

    @OneToMany(mappedBy = "commentOwner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OfferComment> userComments = new HashSet<>();

    @ManyToMany(mappedBy = "offerPositives", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TheOffer> positiveOffers = new HashSet<>();

    @ManyToMany(mappedBy = "offerNegatives", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<TheOffer> negativeOffers = new HashSet<>();

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

    public OfferComment obtainLatestComment() {
        if (getUserComments() == null || getUserComments().isEmpty()) {
            return null;
        }
        return sortByDateAndGetLastComment();
    }

    public TheOffer obtainLatestOffer() {
        if (getUserOffers() == null || getUserOffers().isEmpty()) {
            return null;
        }
        return sortByDateAndGetLastOffer();
    }

    private TheOffer sortByDateAndGetLastOffer() {
        List<TheOffer> offers = getSortedOffers();
        return offers.get(0);
    }

    private OfferComment sortByDateAndGetLastComment() {
        List<OfferComment> comments = getSortedComments();
        return comments.get(comments.size() - 1);
    }

    private List<TheOffer> getSortedOffers() {
        List<TheOffer> offers = new ArrayList<>(getUserOffers());
        Collections.sort(offers, new OfferComparator());
        return offers;
    }

    private List<OfferComment> getSortedComments() {
        List<OfferComment> comments = new ArrayList<>(getUserComments());
        Collections.sort(comments, new CommentComparator());
        return comments;
    }

    public void addPositiveFeedback(TheOffer theOffer) {
        notNull(theOffer, "Atempting to add null offer object to positive feedback list");
        if (this.positiveOffers == null) {
            this.positiveOffers = new HashSet<>();
        }
        this.positiveOffers.add(theOffer);
        theOffer.addPositiveUser(this);
    }

    public void addNegativeFeedback(TheOffer theOffer) {
        notNull(theOffer, "Atempting to add null offer object to negative feedback list");
        if (this.negativeOffers == null) {
            this.negativeOffers = new HashSet<>();
        }
        theOffer.addNegativeUser(this);
        this.negativeOffers.add(theOffer);
    }

    public Set<TheOffer> getPositiveOffers() {
        return this.positiveOffers != null ? this.positiveOffers : new HashSet<TheOffer>();
    }

    public Set<TheOffer> getNegativeOffers() {
        return this.negativeOffers != null ? this.negativeOffers : new HashSet<TheOffer>();
    }

    public boolean feedbackHasAlreadyBeenPerformedForOffer(Long id) {
        return positiveHasBeenPerformed(id) || negativeHasBeenPerformed(id);
    }

    public boolean positiveHasBeenPerformed(Long id) {
        return idMatchInSet(id, getPositiveOffers());
    }

    public boolean negativeHasBeenPerformed(Long id) {
        return idMatchInSet(id, getNegativeOffers());
    }

    public boolean idMatchInSet(Long id, Set<TheOffer> offers) {
        for (TheOffer offer : offers) {
            if (id == offer.getId()) {
                return true;
            }
        }
        return false;
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
}
