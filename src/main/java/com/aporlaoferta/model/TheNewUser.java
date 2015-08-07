package com.aporlaoferta.model;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 7/08/15
 * Time: 12:36
 */
public class TheNewUser extends TheUser {
    private String oldPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
