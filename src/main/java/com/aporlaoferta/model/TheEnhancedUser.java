package com.aporlaoferta.model;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 7/08/15
 * Time: 12:36
 */
public class TheEnhancedUser extends TheUser {
    private String oldPassword;

    private String userSpecifiedEmail;
    private String userSpecifiedPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getUserSpecifiedEmail() {
        return userSpecifiedEmail;
    }

    public void setUserSpecifiedEmail(String userSpecifiedEmail) {
        this.userSpecifiedEmail = userSpecifiedEmail;
    }

    public String getUserSpecifiedPassword() {
        return userSpecifiedPassword;
    }

    public void setUserSpecifiedPassword(String userSpecifiedPassword) {
        this.userSpecifiedPassword = userSpecifiedPassword;
    }
}
