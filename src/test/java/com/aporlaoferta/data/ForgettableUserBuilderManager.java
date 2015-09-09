package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 19/01/2015
 * Time: 17:58
 */
public class ForgettableUserBuilderManager {

    public static ForgettableUserBuilder aForgettableUserWithNicknameAndCode(String nickname, String code) {
        return createBasicBuilder(nickname, code)
                .withPass1("sas")
                .withPass2("sas")
                ;
    }

    public static ForgettableUserBuilder aForgettableUserWithDifferentPass(String nickname, String code) {
        return createBasicBuilder(nickname, code)
                .withPass1("sas1")
                .withPass2("sas2")
                ;
    }

    private static ForgettableUserBuilder createBasicBuilder(String nickname, String code) {
        return ForgettableUserBuilder.aForgettableUser()
                .withNickname(nickname)
                .withUUID(code);
    }

}
