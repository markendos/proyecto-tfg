package es.upo.witzl.proyectotfg.users.security;

import es.upo.witzl.proyectotfg.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component("myAuthenticationSuccessHandler")
public class CustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException {
        String redirectTo = "/login";
        final HttpSession session = request.getSession(false);

        if (session != null) {
            session.setMaxInactiveInterval(60*30); //30 minutes session time out
            MyUserPrincipal user = (MyUserPrincipal) authentication.getPrincipal();
            String email = user.getEmail();
            String username = user.getUsername();
            LoggedUser sessionUser = new LoggedUser(email, username, user.isAdmin(), getClientIP(request));
            activeUserStore.addUser(sessionUser);
            session.setAttribute("user", sessionUser);
            addWelcomeCookie(user.getEmail(), response);

            if (user.isAdmin()) {
                redirectTo = "/console";
            } else {
                redirectTo = "/home";
            }
        }
        redirectStrategy.sendRedirect(request, response, redirectTo);
        clearAuthenticationAttributes(request);
    }

    private String getEmail(final Authentication authentication) {
        return ((MyUserPrincipal) authentication.getPrincipal()).getEmail();
    }

    private void addWelcomeCookie(final String user, final HttpServletResponse response) {
        Cookie welcomeCookie = getWelcomeCookie(user);
        response.addCookie(welcomeCookie);
    }

    private Cookie getWelcomeCookie(final String user) {
        Cookie welcomeCookie = new Cookie("welcome", user);
        welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        return welcomeCookie;
    }

    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }

    protected void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }
}
