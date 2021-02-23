package es.upo.witzl.proyectotfg.users.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.CustomLoginAuthenticationSuccessHandler;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import es.upo.witzl.proyectotfg.util.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@RestController
public class LoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    ApplicationContext ctx;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @PostMapping("/oauth_login")
    public GenericResponse getLoginPage(final HttpServletRequest request, final HttpServletResponse response,
                                        @RequestParam("idtoken") final String idToken) throws GeneralSecurityException,
            IOException {
        final String userEmail = verifyToken(idToken, clientId);
        String message = "auth.message.error";
        if(!userEmail.isEmpty()) {
            Optional<User> opt = userService.getUserByEmail(userEmail);
            if(opt.isPresent()) {
                User user = opt.get();

                if(user.isEnabled()) {
                    final Authentication authentication = userService.authGoogleSignIn(new MyUserPrincipal(user));
                    CustomLoginAuthenticationSuccessHandler loginSuccessHandler = ctx.getBean("myAuthenticationSuccessHandler",CustomLoginAuthenticationSuccessHandler.class);
                    loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
                    message = "success";
                } else {
                    message = "auth.message.disabled";
                }
            }else {
                message = "auth.message.invalidUser";
            }
        }
        return new GenericResponse(message);
    }

    private String verifyToken(final String idTokenString, final String clientId) throws GeneralSecurityException, IOException {
        String email = "";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(clientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Get profile information from payload
            email = payload.getEmail();
        }
        return email;
    }
}
