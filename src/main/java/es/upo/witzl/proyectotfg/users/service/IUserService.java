package es.upo.witzl.proyectotfg.users.service;

import es.upo.witzl.proyectotfg.users.dto.UserDto;
import es.upo.witzl.proyectotfg.users.error.UserAlreadyExistException;
import es.upo.witzl.proyectotfg.users.error.UsernameTakenException;
import es.upo.witzl.proyectotfg.users.model.*;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
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

    Optional<User> getUserByEmail(String email);

    User findByUsername(String username);

    void changeUserPassword(User user, String password);

    Authentication authWithoutPassword(MyUserPrincipal user);

    void revokeAuthentication();

    Authentication authGoogleSignIn(MyUserPrincipal user);

    boolean checkIfValidOldPassword(User user, String password);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    boolean hasVerificationToken(User user);

    boolean hasPassResetToken(User user);

    String validateVerificationToken(String token, String username);

    String validatePasswordResetToken(String token, String username);

    Role getRole(String role);

    void expireToken(Token token);
}
