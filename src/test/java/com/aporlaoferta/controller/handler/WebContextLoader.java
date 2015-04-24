package com.aporlaoferta.controller.handler;

import com.aporlaoferta.controller.GenericWebContextLoader;

/**
 * Created with IntelliJ IDEA.
 * User: hinan
 * Date: 03/02/2015
 * Time: 15:00
 * Hitachi Capital (UK) PLC
 */
class WebContextLoader extends GenericWebContextLoader {

    public WebContextLoader() {
        super("src/test/resources/META-INF/web-resources", false);
    }

}
