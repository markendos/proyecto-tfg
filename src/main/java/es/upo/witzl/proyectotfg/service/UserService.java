package es.upo.witzl.proyectotfg.service;

import es.upo.witzl.proyectotfg.dto.UserDto;
import es.upo.witzl.proyectotfg.error.UserAlreadyExistException;
import es.upo.witzl.proyectotfg.error.UsernameTakenException;
import es.upo.witzl.proyectotfg.model.*;
import es.upo.witzl.proyectotfg.repository.PasswordResetTokenRepository;
import es.upo.witzl.proyectotfg.repository.RoleRepository;
import es.upo.witzl.proyectotfg.repository.UserRepository;
import es.upo.witzl.proyectotfg.repository.VerificationTokenRepository;
import es.upo.witzl.proyectotfg.security.MyUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    public static final String TOKEN_INVALID = "invalid";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Override
    public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExists(accountDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: " + accountDto.getEmail());
        }else if(usernameExists(accountDto.getUsername())) {
            throw new UsernameTakenException("The username: " + accountDto.getUsername() + " is already taken");
        }
        final User user = new User();

        user.setEmail(accountDto.getEmail());
        user.setUsername(accountDto.getUsername());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        //user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        user.setRole(roleRepository.findByName("ROLE_USER"));
        return userRepository.save(user);
    }

    @Override
    public VerificationToken getVerificationToken(final String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public void saveUser(final User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        final VerificationToken verificationToken = verificationTokenRepository.findByUser(user);

        if (verificationToken != null) {
            verificationTokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordToken = passwordTokenRepository.findByUser(user);

        if (passwordToken != null) {
            passwordTokenRepository.delete(passwordToken);
        }

        userRepository.delete(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public String createVerificationTokenForUser(final User user) {
        final String token = UUID.randomUUID().toString();
        final VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);

        return token;
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = verificationTokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID()
                .toString());
        vToken = verificationTokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public PasswordResetToken generateNewPassResetToken(final String existingPasResetToken) {
        PasswordResetToken rToken = passwordTokenRepository.findByToken(existingPasResetToken);
        rToken.updateToken(UUID.randomUUID()
                .toString());
        rToken = passwordTokenRepository.save(rToken);
        return rToken;
    }

    @Override
    public String createPasswordResetTokenForUser(final User user) {
        final String token = UUID.randomUUID().toString();
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);

        return token;
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByVerificationToken(final String token) {
        return Optional.ofNullable(verificationTokenRepository.findByToken(token).getUser());
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordTokenRepository.findByToken(token).getUser());
    }

    @Override
    public Optional<User> getUserById(final String email) {
        return userRepository.findById(email);
    }

    public User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.getResetToken().setExpiryDate(Calendar.getInstance().getTime()); //expire current token
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public boolean usernameExists(final String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public String validateVerificationToken(String token, String username) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        String result = TOKEN_INVALID;

        if (isTokenFound(verificationToken)) {
            if (isTokenExpired(verificationToken)) {
                result = TOKEN_EXPIRED;
            } else if (verificationToken.getUser().getUsername().equals(username)) {
                result = TOKEN_VALID;
            }
        }
        return result;
    }

    @Override
    public String validatePasswordResetToken(String token, String username) {
        final PasswordResetToken passResetToken = passwordTokenRepository.findByToken(token);
        String result = TOKEN_INVALID;

        if (isTokenFound(passResetToken)) {
            if (isTokenExpired(passResetToken)) {
                result = TOKEN_EXPIRED;
            } else if (passResetToken.getUser().getUsername().equals(username)) {
                result = TOKEN_VALID;
            }
        }
        return result;
    }

    @Override
    public Role getRole(String role) {
        return roleRepository.findByName(role);
    }

    @Override
    public Authentication authWithoutPassword(MyUserPrincipal user) {
        List<GrantedAuthority> authorities = new ArrayList();
        authorities.add(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
    @Override
    public void revokeAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            authentication.setAuthenticated(false);
        }
    }

    @Override
    public Authentication authGoogleSignIn(MyUserPrincipal user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }

    private boolean isTokenFound(Token token) {
        return token != null;
    }

    private boolean isTokenExpired(Token token) {
        return token.isExpired();
    }
}
