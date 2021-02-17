package es.upo.witzl.proyectotfg.service;

import es.upo.witzl.proyectotfg.dto.UserDto;
import es.upo.witzl.proyectotfg.error.UserAlreadyExistException;
import es.upo.witzl.proyectotfg.error.UsernameTakenException;
import es.upo.witzl.proyectotfg.model.PasswordResetToken;
import es.upo.witzl.proyectotfg.model.Role;
import es.upo.witzl.proyectotfg.model.User;
import es.upo.witzl.proyectotfg.model.VerificationToken;
import es.upo.witzl.proyectotfg.security.MyUserPrincipal;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException, UsernameTakenException;

    void saveUser(User user);

    void deleteUser(User user);

    List<User> getUsers();

    String createVerificationTokenForUser(User user);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    PasswordResetToken generateNewPassResetToken(String token);

    String createPasswordResetTokenForUser(User user);

    PasswordResetToken getPasswordResetToken(String token);

    Optional<User> getUserByVerificationToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    Optional<User> getUserById(String email);

    User findByUsername(String username);

    void changeUserPassword(User user, String password);

    Authentication authWithoutPassword(MyUserPrincipal user);

    void revokeAuthentication();

    Authentication authGoogleSignIn(MyUserPrincipal user);

    boolean checkIfValidOldPassword(User user, String password);

    boolean usernameExists(final String username);

    boolean emailExists(final String email);

    String validateVerificationToken(String token, String username);

    String validatePasswordResetToken(String token, String username);

    Role getRole(String role);
}
