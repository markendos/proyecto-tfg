package es.upo.witzl.proyectotfg.security;


import es.upo.witzl.proyectotfg.model.Privilege;
import es.upo.witzl.proyectotfg.model.Role;
import es.upo.witzl.proyectotfg.model.User;
import es.upo.witzl.proyectotfg.repository.RoleRepository;
import es.upo.witzl.proyectotfg.repository.UserRepository;
import es.upo.witzl.proyectotfg.validation.ValidEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messages;


    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        try {
            final User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException(messages.getMessage("auth.message.invalidUser", null, Locale.getDefault()) + email);
            }
            return new MyUserPrincipal(user);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
