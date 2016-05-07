package com.aporlaoferta.data;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/2015
 * Time: 17:58
 */
public class EmailTemplateBuilderManager {

    private static final String EMAIL_CONTENT_HEAD = "<html><body><div style=\"background-color:#f38630;border-radius: 2em;\"><div style=\"background-color:#ffffff;border-radius:2em;text-align:left;margin:4em 4em 4em 4em;background-image: url('https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/background_head5.png');\"><div style=\"text-align:center;\"><img style=\"width:100px;height:100px;\" src=\"https://s3-us-west-2.amazonaws.com/aporlaofertaimages/static/logo2.png\"/></div><div style=\"margin-top:1em;text-align:center;\"><img src=\"${avatarsrc}\" style=\"width: 200px;height: 200px;text-align:left;border: solid 3px #ADADAD;border-radius: 20px;\"/><br/>Hola ${nickname}!<br/></div>";
    private static final String EMAIL_CONTENT_TAIL = "<div style=\"text-align:center;\">Nos vemos en <a href=\"${server}\" target=\"_blank\"> aporlaoferta.com!</a></div><div style=\"text-align:center;font-size:7px;\">aporlaoferta 2015</div></div></body></html>";

    private static final String REAL_EMAIL_CONTENT = EMAIL_CONTENT_HEAD + "<div style=\"text-align:center;margin-top:1em;\">Para confirmar tu usuario sigue el siguiente enlace:<br/><br/><h1><a style=\"margin-top:1em;background-color: #51B1EC;padding:.8em;color:#ffffff;border-radius:.5em;border:solid 2px #3F9BEC;\" href=\"${server}/confirmUser?user=${nickname}&confirmationID=${userid}\" target=\"_blank\">confirmar usuario</a></h1><br/><div style=\"text-align:center;margin-left:1em;\"><h5>O copia y pega la siguiente dirección url en tu navegador:<br/>${server}/confirmUser?user=${nickname}&confirmationID=${userid}</h5></div></div>" + EMAIL_CONTENT_TAIL;

    private static final String REAL_FORGOTTEN_EMAIL_CONTENT = EMAIL_CONTENT_HEAD +
            "<div style=\"text-align:center;margin-top:1em;\">Si NO has realizado una petición para reiniciar la contraseña en aporlaoferta.com " +
            "puedes ignorar este mensaje.<br/>" +
            "Para reiniciar tu contraseña de usuario sigue el siguiente enlace:<br/><br/>" +
            "<h1><a style=\"margin-top:1em;background-color: #51B1EC;padding:.8em;color:#ffffff;border-radius:.5em;border:solid 2px #3F9BEC;\" " +
            "href=\"${server}/passwordForgotten?user=${nickname}&track=${userid}\" target=\"_blank\">reiniciar contraseña</a></h1>" +
            "<br/>" +
            "<div style=\"text-align:center;margin-left:1em;\"><h5>O copia y pega la siguiente dirección url en tu navegador:<br/>" +
            "${server}/passwordForgotten?user=${nickname}&track=${userid}</h5></div>" +
            "</div>" +
            EMAIL_CONTENT_TAIL;

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

    public static EmailTemplateBuilder aRealPassowrdForgottenEmailTemplate(String templateName) {
        return EmailTemplateBuilder.anEmailTemplate()
                .withName(templateName)
                .withSubject("Petición de cambio de contraseña aporlaoferta.com")
                .withContent(REAL_FORGOTTEN_EMAIL_CONTENT)
                ;
    }
}
