package com.aporlaoferta.data;

import com.aporlaoferta.model.OfferComment;
import com.aporlaoferta.model.TheEnhancedUser;
import com.aporlaoferta.model.TheOffer;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class EnhancedUserBuilder extends UserBuilder {

    public static EnhancedUserBuilder anEnhancedUser() {
        return new EnhancedUserBuilder();
    }

    public EnhancedUserBuilder() {
        super();
    }

    public EnhancedUserBuilder withNickName(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public EnhancedUserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public EnhancedUserBuilder isEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public EnhancedUserBuilder isPending(boolean pending) {
        this.pending = pending;
        return this;
    }

    public EnhancedUserBuilder withEmail(String mail) {
        this.mail = mail;
        return this;
    }

    public EnhancedUserBuilder withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public EnhancedUserBuilder withCreatedOffers(Set<TheOffer> createdOffers) {
        this.offers = createdOffers;
        return this;
    }

    public EnhancedUserBuilder withCreatedComments(Set<OfferComment> comments) {
        this.comments = comments;
        return this;
    }

    public EnhancedUserBuilder withUUID(String uuid) {
        this.uuid = uuid;
        return this;

    }

    public TheEnhancedUser build() {
        TheEnhancedUser theUser = new TheEnhancedUser();
        theUser.setEnabled(this.enabled);
        theUser.setUserAvatar(this.avatar);
        theUser.setUserComments(this.comments);
        theUser.setPending(this.pending);
        theUser.setUserNickname(this.nickname);
        theUser.setUserOffers(this.offers);
        theUser.setUuid(this.uuid);
        theUser.setUserSpecifiedEmail(this.mail);
        theUser.setUserSpecifiedPassword(this.password);
        return theUser;
    }
}
