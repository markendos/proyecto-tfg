package es.upo.witzl.proyectotfg.users.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

public class LoginPageInterceptor extends HandlerInterceptorAdapter {
    UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean result = true;
        final String url = urlPathHelper.getLookupPathForRequest(request);

        if ((url.startsWith("/login") || url.equals("/")) && isAuthenticated() && isLoggedUser()) {
            String redirect = url;
            MyUserPrincipal user = (MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (user != null) {
                if (user.isAdmin()) {
                    redirect = "/console";
                } else {
                    redirect = "/home";
                }

                String encodedRedirectURL = response.encodeRedirectURL(
                        request.getContextPath() + redirect);

                response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
                response.setHeader("Location", encodedRedirectURL);

                result = false;
            }

        }
        return result;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    private boolean isLoggedUser() {
        boolean result = false;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof MyUserPrincipal) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for(GrantedAuthority authority: authorities) {
                if (authority.getAuthority().contains("ROLE_USER") || authority.getAuthority().contains("ROLE_ADMIN")) {
                    result = true;
                }
            }
        }
        return result;
    }
}
