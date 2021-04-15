package es.upo.witzl.proyectotfg.users.controller;

import es.upo.witzl.proyectotfg.users.dto.PasswordDto;
import es.upo.witzl.proyectotfg.users.dto.UserDto;
import es.upo.witzl.proyectotfg.users.error.InvalidOldPasswordException;
import es.upo.witzl.proyectotfg.users.model.PasswordResetToken;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.model.VerificationToken;
import es.upo.witzl.proyectotfg.users.registration.OnRegistrationCompleteEvent;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import es.upo.witzl.proyectotfg.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.ModelAndView;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

@RestController
public class RegistrationController {

    @Autowired
    private IUserService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;

    @Autowired
    private LocaleContextResolver localeResolver;

    public RegistrationController() {
        super();
    }

    // Registration
    @PostMapping("/user/registration")
    public GenericResponse registerUserAccount(@Valid final UserDto accountDto,
                                               final HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        final User registered = userService.registerNewUserAccount(accountDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, locale, getAppUrl(request)));

        return new GenericResponse("success");
    }

    // User activation - verification
    @GetMapping("/registrationConfirm")
    public ModelAndView confirmRegistration(final ModelMap model, @RequestParam("username") final String username,
                                            @RequestParam("token") final String token) {
        String redirect = "";
        String message = "";
        final String result = userService.validateVerificationToken(token, username);

        if (!result.equals("invalid")) {
            Optional<User> opt = userService.getUserByVerificationToken(token);

            if (opt.isPresent() && opt.get().getUsername().equals(username)) {
                User user = opt.get();
                if(result.equals("valid")) {
                    user.setEnabled(true);
                    userService.saveUser(user);
                    message = "message.accountVerified";
                    redirect = "redirect:/login?message=" + message;
                }else if(result.equals("expired")) {
                    message = "auth.message.expired";
                    redirect = "badUser";
                    model.addAttribute("email", user.getEmail());
                    model.addAttribute("token", token);
                    model.addAttribute("message", message);
                }
            }
        }
        else {
            message="auth.message.invalidToken";
            redirect = "redirect:/login?error=" + message;
        }
        return new ModelAndView(redirect, model);
    }

    @GetMapping("/user/resendRegistrationToken")
    public GenericResponse resendRegistrationToken(final HttpServletRequest request,
                                                   @RequestParam("token") final String existingToken) {

        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        Locale locale = localeResolver.resolveLocale(request);
        Optional<User> opt = userService.getUserByVerificationToken(newToken.getToken());
        String message = "";

        if (opt.isPresent()) {
            User user = opt.get();
            mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), locale, newToken, user));
            message = "message.resendToken";
        } else {
            message = "auth.message.invalidToken";
        }
        return new GenericResponse(message);
    }

    // Reset password
    @PostMapping("/user/resetPassword")
    public GenericResponse resetPassword(final HttpServletRequest request,
                                         @RequestParam("email") final String emailEncoded) {
        Locale locale = localeResolver.resolveLocale(request);
        String userEmail = decodeBase64(emailEncoded);
        final Optional<User> opt = userService.getUserByEmail(userEmail);
        String message = "";

        if (opt.isPresent()) {
            User user = opt.get();
            if(user.isEnabled()) {
                PasswordResetToken existingToken = user.getResetToken();
                String token = "";
                if (existingToken != null) {
                    token = userService.generateNewPassResetToken(existingToken.getToken()).getToken();
                } else {
                    token = userService.createPasswordResetTokenForUser(user);
                }
                mailSender.send(constructResetTokenEmail(getAppUrl(request), locale, token, user));
                message = "message.resetPasswordEmail";
            } else {
                message = "auth.message.disabled";
            }
        } else {
            message = "message.badEmail";
        }
        return new GenericResponse(message);
    }

    // Change user password
    @GetMapping("/user/changePassword")
    public ModelAndView changePassword(final ModelMap model, @RequestParam("username") final String username,
                                       @RequestParam("token") final String token) {
        String redirect = "";
        String message = "";
        String result = userService.validatePasswordResetToken(token, username);

        if (result.equals("valid")) {
            PasswordResetToken resetToken = userService.getPasswordResetToken(token);
            userService.authWithoutPassword(new MyUserPrincipal(resetToken.getUser()));
            model.addAttribute("token", token);
            model.addAttribute("username", username);
            redirect = "updatePassword";
        } else {
            if (result.equals("expired")) {
                message = "aut.message.resetToken.expired";
            } else {
                message = "auth.message.invalidToken";
            }
            redirect = "redirect:/login?message=" + message;
        }
        return new ModelAndView(redirect, model);
    }

    // Save password
    @PostMapping("/user/savePassword")
    public GenericResponse savePassword(@Valid PasswordDto passwordDto) {
        final String result = userService.validatePasswordResetToken(passwordDto.getToken(), passwordDto.getUsername());
        String message = "";

        if (result == "valid") {
            Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());

            if (user.isPresent()) {
                userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
                message = "message.resetPasswordSuc";
            } else {
                message = "auth.message.invalid";
            }
        } else {
            message = "auth.message.";
        }
        return new GenericResponse(message);
    }

    @PostMapping("/user/updatePassword")
    public GenericResponse changeUserPassword(@Valid PasswordDto passwordDto) {
        final User user = userService.getUserByEmail(((MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail()).get();

        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new InvalidOldPasswordException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse("message.updatePasswordSuc");
    }

// ============== NON-API ============

    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale,
                                                                    final VerificationToken newToken, final User user) {
        final String defaultMessage = "You registered successfully." +
                "To confirm your registration, please click on the link bellow:";
        final String subject = HtmlEscape.unescapeHtml(messages.getMessage("message.confirmation.email.subject",
                null, "Confirm sign up", locale));
        final String confirmationUrl = contextPath + "/registrationConfirm?username=" + user.getUsername() + "&token=" +
                newToken.getToken();
        final String message = HtmlEscape.unescapeHtml(messages.getMessage("message.regSuccLink",
                Arrays.asList(user.getUsername()).toArray(), defaultMessage, locale));

        return constructEmail(subject, message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token,
                                                       final User user) {
        final String url = contextPath + "/user/changePassword?username=" + user.getUsername() + "&token=" + token;
        final String message = HtmlEscape.unescapeHtml(messages.getMessage("message.resetPassword.email.body",
                null, locale));
        final String subject = HtmlEscape.unescapeHtml(messages.getMessage("message.resetPassword.email.subject",
                null, locale));
        return constructEmail(subject, message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return env.getProperty("web.protocol") + "://" + request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath();
    }

    private String decodeBase64(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes);
    }
}
