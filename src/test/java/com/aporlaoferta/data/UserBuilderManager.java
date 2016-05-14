package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 17:58
 */
public class UserBuilderManager {

    public static UserBuilder aRegularUserWithNickname(String nickname) {
        return UserBuilder.aUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail("duckU@duckermuther.com")
                .withNickName(nickname)
                .withPassword("thisistheencodedPassword")
                .isPending(false)
                .isEnabled(true)
                .withUUID("LKADJSFLKSADF")
                ;
    }

    public static UserBuilder aPendingUserWithNickname(String nickname) {
        return UserBuilder.aUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail("duckU@duckermuther.com")
                .withNickName(nickname)
                .withPassword("thisistheencodedPassword")
                ;
    }

    public static UserBuilder aRegularUserWithEmail(String existingEmail) {
        return UserBuilder.aUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail(existingEmail)
                .withNickName("duck")
                .withPassword("thisistheencodedPassword")
                ;
    }
}
