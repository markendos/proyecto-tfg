package es.upo.witzl.proyectotfg.spring;

import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.repository.SensorRepository;
import es.upo.witzl.proyectotfg.users.model.Privilege;
import es.upo.witzl.proyectotfg.users.model.Role;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.repository.PrivilegeRepository;
import es.upo.witzl.proyectotfg.users.repository.RoleRepository;
import es.upo.witzl.proyectotfg.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;
    ;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SensorRepository sensorRepository;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // create initial privileges
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");

        // create initial roles
        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, passwordPrivilege));
        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        final Role userRole = createRoleIfNotFound("ROLE_USER", userPrivileges);

        // create initial users
        createUserIfNotFound("admin@test.com", "Admin", "admin", adminRole);
        createUserIfNotFound("user1@test.com", "User1", "u$er1", userRole);
        createUserIfNotFound("user2@test.com", "User2", "u$er2", userRole);

        // create initial sensors
        createSensorIfNotFound("Default", "RAW", "Generic sensor");

        alreadySetup = true;
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

    @Transactional
    User createUserIfNotFound(final String email, final String username, final String password, final Role role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
        }
        user.setRole(role);
        user = userRepository.save(user);
        return user;
    }

    @Transactional
    Sensor createSensorIfNotFound(final String name, final String alias, final String description) {
        Optional<Sensor> sensorOptional = sensorRepository.findByAlias(alias);
        Sensor sensor;
        if (!sensorOptional.isPresent()) {
            sensor = new Sensor();
            sensor.setName(name);
            sensor.setAlias(alias);
            sensor.setDescription(description);
        } else {
            sensor = sensorOptional.get();
        }
        sensor = sensorRepository.save(sensor);
        return sensor;
    }
}
