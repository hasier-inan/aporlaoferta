package com.aporlaoferta.model;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 7/08/15
 * Time: 12:36
 */
public class TheForgettableUser extends TheUser {
    private String firstPassword;
    private String secondPassword;
    private String track;

    public String getFirstPassword() {
        return firstPassword;
    }

    public void setFirstPassword(String firstPassword) {
        this.firstPassword = firstPassword;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public boolean passMatches() {
        return !isEmpty(firstPassword)
                && !isEmpty(secondPassword)
                && firstPassword.equals(secondPassword);
    }
}
