package com.aporlaoferta.data;

import com.aporlaoferta.model.TheUserRoles;
import com.aporlaoferta.model.UserRoles;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 14/11/2014
 * Time: 16:00
 */
public class UserRoleBuilder implements Serializable{

    private String userNickname;
    private UserRoles userRole;

    public static UserRoleBuilder aUserRole() {
        return new UserRoleBuilder();
    }

    public UserRoleBuilder() {
        super();
    }

    public UserRoleBuilder withNickName(String nickname) {
        this.userNickname = nickname;
        return this;
    }

    public UserRoleBuilder withUserRole(UserRoles role) {
        this.userRole = role;
        return this;
    }

    public TheUserRoles build() {
        TheUserRoles theUserRoles = new TheUserRoles();
        theUserRoles.setUserNickname(this.userNickname);
        theUserRoles.setUserRole(this.userRole);
        return theUserRoles;
    }
}
