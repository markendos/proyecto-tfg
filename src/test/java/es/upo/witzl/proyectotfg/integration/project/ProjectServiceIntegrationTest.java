package es.upo.witzl.proyectotfg.integration.project;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.projects.service.ILabelService;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.projects.service.ProjectService;
import es.upo.witzl.proyectotfg.users.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class ProjectServiceIntegrationTest {

    @TestConfiguration
    static class ProjectServiceTestContextConfiguration {
        @Bean
        public IProjectService projectService() {
            return new ProjectService();
        }
    }

    @Autowired
    private IProjectService projectService;

    @MockBean
    private ILabelService labelService;

    @MockBean
    private ProjectRepository projectRepository;

    private User testUser;

    @Before
    public void setUp() {
        User user1 = new User();
        user1.setEmail("testuser1@test.com");
        user1.setUsername("testuser1");

        testUser = user1;

        User user2 = new User();
        user2.setEmail("testuser2@test.com");
        user2.setUsername("testuser2");

        Project project1 = new Project();
        project1.setId(Long.parseLong("1"));
        project1.setName("project1_name");
        project1.setDescription("project1_description");
        project1.setStartDate(new Date());
        project1.setUser(user1);

        Project project2 = new Project();
        project2.setId(Long.parseLong("2"));
        project2.setName("project2_name");
        project2.setDescription("projec2_description");
        project2.setStartDate(new Date());
        project2.setUser(user2);

        Project project3 = new Project();
        project3.setId(Long.parseLong("3"));
        project3.setName("project3_name");
        project3.setDescription("project3_description");
        project3.setStartDate(new Date());
        project3.setUser(user2);

        List<Project> user1ForeignProjects = Arrays.asList(project2, project3);

        Mockito.when(projectRepository.findById(project1.getId())).thenReturn(Optional.of(project1));
        Mockito.when(projectRepository.findById(project2.getId())).thenReturn(Optional.of(project2));
        Mockito.when(projectRepository.findByUserNotLike(user1)).thenReturn(user1ForeignProjects);
    }

    @Test
    public void whenValidId_thenProjectShouldBeFound() {
        Long id = Long.parseLong("1");

        Optional<Project> foundOpt = projectService.getProjectById(id);
        assertTrue(foundOpt.isPresent());

        Project found = foundOpt.get();
        assertEquals(found.getId(), id);
    }

    @Test
    public void whenInValidId_thenProjectShouldNotBeFound() {
        Long id = Long.parseLong("-1");

        Optional<Project> foundOpt = projectService.getProjectById(id);
        assertFalse(foundOpt.isPresent());
    }

    @Test
    public void whenGetForeignProjectsOfUser_thenForeignProjectListShouldBeReturned() {
        List<Project> foreignProjects = projectService.getForeignProjects(testUser);

        verifyFindByUserNotLikeCalledOnce(testUser);
        assertThat(foreignProjects).hasSize(2).extracting(Project::getId).contains(Long.parseLong("2"), Long.parseLong("3"));
    }

    private void verifyFindByUserNotLikeCalledOnce(User u) {
        Mockito.verify(projectRepository, VerificationModeFactory.times(1)).findByUserNotLike(u);
        Mockito.reset(projectRepository);
    }
}
