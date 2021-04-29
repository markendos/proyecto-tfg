package es.upo.witzl.proyectotfg.configuration;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
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

    @Autowired
    private ProjectRepository projectRepository;

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
        createUserIfNotFound("admin@mg.tfg-mwd.me", "Admin", "qE9P)<r-mtFz!QQ4", adminRole);
        createUserIfNotFound("user1@mg.tfg-mwd.me", "User1", ":F(7t@Q=J&Sng8=8", userRole);
        createUserIfNotFound("user2@mg.tfg-mwd.me", "User2", "BnW[CVh%T<gk%8gK", userRole);

        // create test data
        createUserIfNotFound("admin@test.test", "Test Admin", "pDkD2SsjtUgq7RMk", adminRole);
        User testUser = createUserIfNotFound("user@test.test", "Test User", "5QbPsdkz65nfXWVe", userRole);
        createProjectIfNotFound("test name", "test description", testUser);

        // create initial sensors
        createSensorIfNotFound("Default Sensor", "RAW", "Generic sensor");
        createSensorIfNotFound("Electromyography Sensor", "EMG",
                "Purpose-built sensor for muscle activity measurement.");
        createSensorIfNotFound("Electroencephalography Sensor", "EEG",
                "Purpose-built sensor for brain activity measurement");
        createSensorIfNotFound("Electrocardiography Sensor", "ECG",
                "Purpose-built sensor to measure the electrical activity of the heart");
        createSensorIfNotFound("Electrodermal Activity Sensor", "EDA",
                "Purpose-built sensor to measure the changes in skin conductance resulting from the sympathetic" +
                        " nervous system activity.");
        createSensorIfNotFound("Accelerometer Sensor", "ACC",
                "3-axis accelerometer for motion measurement.");
        createSensorIfNotFound("Temperature Sensor", "TMP",
                "Calibrated multi-purpose sensor for basic body and environmental temperature measurement.");
        createSensorIfNotFound("Electrooculography Sensor", "EOG",
                "Purpose-built sensor for corneo-retinal standing potential activity measurement.");
        createSensorIfNotFound("Electrogastrography Sensor", "EGG", "Purpose-built sensor for gastric " +
                "activity measurement.");
        createSensorIfNotFound("Respiration Sensor", "PZT",
                "Easy-to-use sensor for measurement of respiratory cycles, complete with interface cable and " +
                        "an adjustable elastic fastening strap.");
        createSensorIfNotFound("FlexiForce Sensor", "FSR",
                "Ultra-thin and flexible piezoresistive sensor, specifically designed for force and pressure " +
                        "measurement.");
        createSensorIfNotFound("Blood Pressure Reader", "BPR",
                "User-friendly cuff-based wrist blood pressure monitor with plug & play interface for real-time " +
                        "data recording with BITalino.");
        createSensorIfNotFound("SpO2 Reader", "OSL",
                "User-friendly finger clip sensor for SpO2 and heart rate monitoring; works as a standalone " +
                        "device or connected to BITalino for real-time data recording.");
        createSensorIfNotFound("Glucose Meter Reader", "GMR",
                "User-friendly glucose meter; works as a standalone device or connected to BITalino for " +
                        "real-time data recording, and includes all the accessories to get started.");

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
    Project createProjectIfNotFound(final String name, final String description, final User user) {
        Project project;
        if (user.getOwnedProjects() == null || user.getOwnedProjects().isEmpty()) {
            project = new Project();
            project.setName(name);
            project.setDescription(description);
            project.setStartDate(new Date());
            project.setUser(user);
        } else {
            List<Project> projects = (List)user.getOwnedProjects();
            project = projects.get(0);
        }
        project = projectRepository.save(project);
        return project;
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
