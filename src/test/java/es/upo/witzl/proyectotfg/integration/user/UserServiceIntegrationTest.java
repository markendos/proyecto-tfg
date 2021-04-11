package es.upo.witzl.proyectotfg.integration.user;

import es.upo.witzl.proyectotfg.users.dto.UserDto;
import es.upo.witzl.proyectotfg.users.error.UserAlreadyExistException;
import es.upo.witzl.proyectotfg.users.error.UsernameTakenException;
import es.upo.witzl.proyectotfg.users.model.Privilege;
import es.upo.witzl.proyectotfg.users.model.Role;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.repository.*;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import es.upo.witzl.proyectotfg.users.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceIntegrationTest {
    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public IUserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private IUserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    @MockBean
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PrivilegeRepository privilegeRepository;


    @Before
    public void setUp() {
        // == create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);
        User user1 = new User();
        user1.setEmail("testuser1@test.com");
        user1.setUsername("testuser1");
        user1.setRole(userRole);

        User user2 = new User();
        user2.setEmail("testuser2@test.com");
        user2.setUsername("testuser2");
        user2.setRole(userRole);

        User user3 = new User();
        user3.setEmail("testuser3@test.com");
        user3.setUsername("testuser3");
        user3.setRole(userRole);

        List<User> allUsers = Arrays.asList(user1, user2);

        Mockito.when(userRepository.findById(user1.getEmail())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(user2.getEmail())).thenReturn(Optional.of(user2));
        Mockito.when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        Mockito.when(userRepository.findByEmail(user2.getEmail())).thenReturn(user2);
        Mockito.when(userRepository.findById("notregistered1@test.com")).thenReturn(Optional.ofNullable(null));
        Mockito.when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        Mockito.when(userRepository.findByUsername(user2.getUsername())).thenReturn(user2);
        Mockito.when(userRepository.findAll()).thenReturn(allUsers);
        Mockito.when(userRepository.save(user3)).thenReturn(user3);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "testuser1@test.com";

        Optional<User> foundOpt = userService.getUserByEmail(email);
        assertTrue(foundOpt.isPresent());

        User found = foundOpt.get();
        assertEquals(found.getEmail(), email);
    }

    @Test
    public void whenInValidEmail_thenUserShouldNotBeFound() {
        Optional<User> notFoundOpt = userService.getUserByEmail("notregistered1@test.com");
        assertFalse(notFoundOpt.isPresent());
    }

    @Test
    public void whenValidEmail_thenUserShouldExist() {
        String email = "testuser2@test.com";
        assertTrue(userService.emailExists(email));
    }

    @Test
    public void whenInValidEmail_thenUserShouldNotExist() {
        String email = "notregistered1@test.com";
        assertFalse(userService.emailExists(email));
    }

    @Test
    public void given2Users_whengetAll_thenReturn2Records() {
        User user1 = new User();
        user1.setEmail("testuser1@test.com");
        user1.setUsername("testuser1");

        User user2 = new User();
        user2.setEmail("testuser2@test.com");
        user2.setUsername("testuser2");

        List<User> allUsers = userService.getUsers();
        verifyFindAllUsersIsCalledOnce();
        assertThat(allUsers).hasSize(2).extracting(User::getEmail).contains(user1.getEmail(), user2.getEmail());
    }

    @Test
    public void whenSaveNewUserDto_thenUserShouldBeReturned() {
        UserDto userDto = new UserDto();
        userDto.setEmail("testuser3@test.com");
        userDto.setUsername("testuser3");
        User newUser = userService.registerNewUserAccount(userDto);
        assertEquals("testuser3@test.com", newUser.getEmail());
        assertEquals("testuser3", newUser.getUsername());
    }

    @Test(expected = UserAlreadyExistException.class)
    public void whenSaveExistingUserEmail_throwsUserAlreadyExistsException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("testuser1@test.com");
        userDto.setUsername("testuserx");
        userService.registerNewUserAccount(userDto);
    }

    @Test(expected = UsernameTakenException.class)
    public void whenSaveExistingUserUsername_throwsUsernameTakenException() {
        UserDto userDto = new UserDto();
        userDto.setEmail("testuserx@test.com");
        userDto.setUsername("testuser1");
        userService.registerNewUserAccount(userDto);
    }

    private void verifyFindAllUsersIsCalledOnce() {
        Mockito.verify(userRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(userRepository);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }
}
