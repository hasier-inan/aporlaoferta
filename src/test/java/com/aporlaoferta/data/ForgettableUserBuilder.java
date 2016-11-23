package com.aporlaoferta.data;

import com.aporlaoferta.model.TheForgettableUser;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class ForgettableUserBuilder {

    private String pass1;
    private String pass2;
    private String track;
    private String nickname;

    public static ForgettableUserBuilder aForgettableUser() {
        return new ForgettableUserBuilder();
    }

    public ForgettableUserBuilder() {
        super();
    }

    public ForgettableUserBuilder withPass1(String pass1) {
        this.pass1 = pass1;
        return this;
    }

    public ForgettableUserBuilder withPass2(String pass2) {
        this.pass2 = pass2;
        return this;
    }

    public ForgettableUserBuilder withTrack(String uuid) {
        this.track = uuid;
        return this;
    }

    public ForgettableUserBuilder withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public TheForgettableUser build() {
        TheForgettableUser theUser = new TheForgettableUser();
        theUser.setUserNickname(this.nickname);
        theUser.setTrack(this.track);
        theUser.setFirstPassword(this.pass1);
        theUser.setSecondPassword(this.pass2);
        return theUser;
    }
}
