package es.upo.witzl.proyectotfg.integration.user;

import es.upo.witzl.proyectotfg.configuration.PersistenceJPAConfig;
import es.upo.witzl.proyectotfg.users.model.Privilege;
import es.upo.witzl.proyectotfg.users.model.Role;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.repository.PrivilegeRepository;
import es.upo.witzl.proyectotfg.users.repository.RoleRepository;
import es.upo.witzl.proyectotfg.users.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = { PersistenceJPAConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@ActiveProfiles("test")
public class UserRepositoryIntegrationTest {

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
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
        User user = new User();
        user.setEmail("testuser@test.com");
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(userRole);

        userRepository.save(user);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "testuser@test.com";
        User found = userRepository.findByEmail(email);

        assertEquals(found.getEmail(), email);
    }

    @Test
    public void whenValidUsername_thenUserShouldBeFound() {
        String username = "testuser";
        User found = userRepository.findByUsername(username);

        assertEquals(found.getUsername(), username);
    }

    @Test
    public void whenInValidEmail_thenUserShouldNotBeFound() {
        String email = "notregistered@test.com";
        User found = userRepository.findByEmail(email);

        assertNull(found);
    }

    @Test
    public void whenInValidUsername_thenUserShouldNotBeFound() {
        String username = "notregistered";
        User found = userRepository.findByUsername(username);

        assertNull(found);
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
