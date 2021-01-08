package es.upo.witzl.proyectotfg.registration.listener;

import es.upo.witzl.proyectotfg.model.User;
import es.upo.witzl.proyectotfg.registration.OnRegistrationCompleteEvent;
import es.upo.witzl.proyectotfg.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.unbescape.html.HtmlEscape;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private IUserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    // API

    @Override
    public void onApplicationEvent(final OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(final OnRegistrationCompleteEvent event) {
        final User user = event.getUser();
        String token = service.createVerificationTokenForUser(user);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    //

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final User user, final String token) {
        final Locale locale = event.getLocale();
        final String recipientAddress = user.getEmail();
        final String defaultMessage = "You registered successfully. To confirm your registration, please click on the link bellow:";
        final String subject = HtmlEscape.unescapeHtml(messages.getMessage("message.confirmation.email.subject", null, "Confirm sign up", locale));
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?username=" + user.getUsername() + "&token=" + token;
        final String message = HtmlEscape.unescapeHtml(messages.getMessage("message.regSuccLink", Arrays.asList(user.getUsername()).toArray(), defaultMessage, locale));
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


}
