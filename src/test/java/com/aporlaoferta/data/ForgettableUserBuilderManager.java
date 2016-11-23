package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 17:58
 */
public class ForgettableUserBuilderManager {

    public static ForgettableUserBuilder aForgettableUserWithNicknameAndCode(String nickname, String code) {
        return createBasicBuilder(nickname)
                .withPass1("sas")
                .withPass2("sas")
                .withTrack(code);
    }

    public static ForgettableUserBuilder aForgettableUserWithDifferentPass(String nickname, String code) {
        return createBasicBuilder(nickname)
                .withPass1("sas1")
                .withPass2("sas2")
                .withTrack(code);
    }

    private static ForgettableUserBuilder createBasicBuilder(String nickname) {
        return ForgettableUserBuilder.aForgettableUser()
                .withNickname(nickname);
    }

}
