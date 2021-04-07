package es.upo.witzl.proyectotfg.users.security;


import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
