package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheOffer;
import com.aporlaoferta.model.TheUser;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class UserBuilder {

    protected String nickname;
    protected String password;
    protected boolean enabled;
    protected boolean pending;
    protected String mail;
    protected String avatar;
    protected String uuid;
    protected Set<TheOffer> offers;
    protected Set<OfferComment> comments;

    public static UserBuilder aUser() {
        return new UserBuilder();
    }

    public UserBuilder() {
        super();
    }

    public UserBuilder withNickName(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder isEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public UserBuilder isPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public UserBuilder withEmail(String mail) {
        this.mail = mail;
        return this;
    }

    public UserBuilder withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public UserBuilder withCreatedOffers(Set<TheOffer> createdOffers) {
        this.offers = createdOffers;
        return this;
    }

    public UserBuilder withCreatedComments(Set<OfferComment> comments) {
        this.comments = comments;
        return this;
    }

    public UserBuilder withUUID(String uuid) {
        this.uuid = uuid;
        return this;

    }

    public TheUser build() {
        TheUser theUser = new TheUser();
        theUser.setEnabled(this.enabled);
        theUser.setUserAvatar(this.avatar);
        theUser.setUserComments(this.comments);
        theUser.setPending(this.pending);
        theUser.setUserEmail(this.mail);
        theUser.setUserNickname(this.nickname);
        theUser.setUserOffers(this.offers);
        theUser.setUserPassword(this.password);
        theUser.setUuid(this.uuid);
        return theUser;
    }
}
