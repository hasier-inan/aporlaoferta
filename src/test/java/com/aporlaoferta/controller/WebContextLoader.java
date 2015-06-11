package com.aporlaoferta.controller;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 03/02/2015
 * Time: 15:00
 */
class WebContextLoader extends GenericWebContextLoader {

    public WebContextLoader() {
        super("src/test/resources/META-INF/web-resources", false);
    }

}
