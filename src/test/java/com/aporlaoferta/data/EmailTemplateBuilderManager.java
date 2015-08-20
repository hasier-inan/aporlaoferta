package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/2015
 * Time: 17:58
 */
public class EmailTemplateBuilderManager {

    private static final String REAL_EMAIL_CONTENT = "<html><body>" +
            "<div style=\"" +
            "background-color:#f38630;" +
            "border-radius: 2em;" +
            "\">" +
            "<div style=\"background-color:#ffffff;border-radius:2em;text-align:left;margin:4em 4em 4em 4em;\">" +
            "<div style=\"text-align:center;\"><img style=\"width:25px;height:25px;\" src=\"${server}/resources/images/logo2.png\"/></div>" +
            "<div style=\"margin-left:1em;margin-top:1em;text-align:center;\">" +
            "<img src=\"${avatarsrc}\" style=\"width:30px;height:30px;text-align:left;border:solid 3px #f38630; border-radius:1em;\"/>" +
            "<br/>Hola ${nickname}!<br/>" +
            "</div>" +
            "<div style=\"text-align:center;margin-top:1em;\">Para confirmar tu usuario sigue el siguiente link:<br/><br/>" +
            "<h1><a style=\"margin-top:1em;background-color: #51B1EC;padding:.8em;color:#ffffff;border-radius:.5em;border:solid 2px #3F9BEC;\" " +
            "href=\"${server}/confirmUser?user=${nickname}&confirmationID=${userid}\" target=\"_blank\">confirmar usuario</a></h1>" +
            "<br/>" +
            "<div style=\"text-align:center;margin-left:1em;\"><h5>O copia y pega la siguiente dirección url en tu navegador:<br/>" +
            "${server}/confirmUser?user=${nickname}&confirmationID=${userid}</h5></div>" +
            "</div>" +
            "<div style=\"text-align:center;\">Nos vemos en <a href=\"${server}\" target=\"_blank\"> aporlaoferta.com!</a></div>" +
            "<div style=\"text-align:center;font-size:7px;\">aporlaoferta 2015</div>" +
            "</div>" +
            "</body></html>";

    public static EmailTemplateBuilder aBasicEmailTemplate(String name) {
        return EmailTemplateBuilder.anEmailTemplate()
                .withId(1L)
                .withName(name)
                .withSubject("this is the super subject")
                .withContent("<html><body>hello ${nickname}<br/> ${avatarsrc} <br/>${userid}</body></html>")
                ;
    }

    public static EmailTemplateBuilder aRealConfirmationEmailTemplate(String templateName) {
        return EmailTemplateBuilder.anEmailTemplate()
                .withName(templateName)
                .withSubject("Bienvenido a aporlaoferta.com!")
                .withContent(REAL_EMAIL_CONTENT)
                ;
    }
}
