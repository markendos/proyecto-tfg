package es.upo.witzl.proyectotfg.integration.project;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.spring.PersistenceJPAConfig;
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
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = { PersistenceJPAConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@ActiveProfiles("test")
public class ProjectRepositoryIntegrationTest {

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private PrivilegeRepository privilegeRepository;


    private Project testProject;

    @Before
    public void setUp() {
        User user = new User();
        user.setEmail("testuser@test.com");
        user.setUsername("testuser");

        Project project = new Project();
        project.setName("project_name");
        project.setDescription("project_description");
        project.setStartDate(new Date());
        project.setUser(userRepository.save(user));

        testProject = projectRepository.save(project);
    }

    @Test
    public void whenValidId_thenProjectShouldBeFound() {
        Long id = testProject.getId();
        String pName = testProject.getName();
        String pDescription = testProject.getDescription();
        Date pDate = testProject.getStartDate();
        User pUser = testProject.getUser();
        Optional<Project> foundOpt = projectRepository.findById(id);
        assertTrue(foundOpt.isPresent());
        Project found = foundOpt.get();
        assertEquals(found.getId(), id);
        assertEquals(found.getName(), pName);
        assertEquals(found.getDescription(), pDescription);
        assertEquals(found.getStartDate(), pDate);
        assertEquals(found.getUser(), pUser);
    }

    @Test
    public void whenInValidId_thenProjectShouldNotBeFound() {
        Long id = Long.parseLong("-1");
        Optional<Project> foundOpt = projectRepository.findById(id);
        assertFalse(foundOpt.isPresent());
    }
}
