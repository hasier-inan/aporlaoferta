package com.aporlaoferta.data;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

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
                .withEmail(String.format("%s@mail.com", UUID.randomUUID().toString()))
                .withNickName(nickname)
                .withPassword("thisistheencodedPassword")
                .isPending(false)
                .isEnabled(true)
                .withUUID("LKADJSFLKSADF")
                ;
    }

    public static EnhancedUserBuilder aRegularEnhancedUserWithNickname(String nickname) {
        return EnhancedUserBuilder.anEnhancedUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail(String.format("%s@mail.com", UUID.randomUUID().toString()))
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
                .withEmail(String.format("%s@mail.com", UUID.randomUUID().toString()))
                .withNickName(nickname)
                .isEnabled(true)
                .withPassword("thisistheencodedPassword")
                ;
    }


    public static EnhancedUserBuilder aPendingEnhancedUserWithNickname(String nickname) {
        return EnhancedUserBuilder.anEnhancedUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail(String.format("%s@mail.com", UUID.randomUUID().toString()))
                .withNickName(nickname)
                .isEnabled(true)
                .withPassword("thisistheencodedPassword")
                ;
    }

    public static UserBuilder aRegularUserWithEmail(String existingEmail) {
        return UserBuilder.aUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail(existingEmail)
                .withNickName("duck")
                .isEnabled(true)
                .withPassword("thisistheencodedPassword")
                ;
    }

    public static EnhancedUserBuilder aRegularEnhancedUserWithEmail(String existingEmail) {
        return EnhancedUserBuilder.anEnhancedUser()
                .withAvatar("http://this.is.my.avatar.jpg")
                .withEmail(existingEmail)
                .withNickName("duck")
                .isEnabled(true)
                .withPassword("thisistheencodedPassword")
                ;
    }
}
