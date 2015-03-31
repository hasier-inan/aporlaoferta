package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
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
                ;
    }
}
