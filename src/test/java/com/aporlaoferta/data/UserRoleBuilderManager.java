package com.aporlaoferta.data;

import com.aporlaoferta.model.UserRoles;

/**
 * Created with IntelliJ IDEA.
 * User: HInan
 * Date: 19/01/2015
 * Time: 17:58
 */
public class UserRoleBuilderManager {

    public static UserRoleBuilder aRoleUser(String nickname) {
        return UserRoleBuilder.aUserRole()
                .withNickName(nickname)
                .withUserRole(UserRoles.ROLE_USER)
                ;
    }
}
