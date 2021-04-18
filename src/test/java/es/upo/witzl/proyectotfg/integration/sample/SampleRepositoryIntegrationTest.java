package es.upo.witzl.proyectotfg.integration.sample;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.samples.model.DataSample;
import es.upo.witzl.proyectotfg.samples.repository.DataSampleRepository;
import es.upo.witzl.proyectotfg.configuration.PersistenceJPAConfig;
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
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = { PersistenceJPAConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@ActiveProfiles("test")
public class SampleRepositoryIntegrationTest {

    @Resource
    private DataSampleRepository sampleRepository;

    @Resource
    private ProjectRepository projectRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private PrivilegeRepository privilegeRepository;


    private DataSample testSample;

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
        project = projectRepository.save(project);

        DataSample sample = new DataSample();
        sample.setProject(project);
        sample.setDeviceName("test_device_name");
        sample.setDeviceModel("test_device_model");
        sample.setDeviceFirmware("test_device_firmware");
        sample.setSampleDate(new Date());
        sample.setSamplingRate(Float.parseFloat("100"));
        sample.setComments("test_comments");
        sample.setSize(999);

        testSample = sampleRepository.save(sample);
    }

    @Test
    public void whenValidId_thenSampleShouldBeFound() {
        Long id = testSample.getId();
        String devName = testSample.getDeviceName();
        String devModel = testSample.getDeviceModel();
        String devFirmware = testSample.getDeviceFirmware();
        Date sDate = testSample.getSampleDate();
        Float sRate = testSample.getSamplingRate();
        String sComments = testSample.getComments();
        Integer sSize = testSample.getSize();
        Project sProject = testSample.getProject();
        Optional<DataSample> foundOpt = sampleRepository.findById(id);
        assertTrue(foundOpt.isPresent());
        DataSample found = foundOpt.get();
        assertEquals(found.getId(), id);
        assertEquals(found.getDeviceName(), devName);
        assertEquals(found.getDeviceModel(), devModel);
        assertEquals(found.getDeviceFirmware(), devFirmware);
        assertEquals(found.getSampleDate(), sDate);
        assertEquals(found.getSamplingRate(), sRate);
        assertEquals(found.getComments(), sComments);
        assertEquals(found.getSize(), sSize);
        assertEquals(found.getProject(), sProject);
    }

    @Test
    public void whenInValidId_thenProjectShouldNotBeFound() {
        Long id = Long.parseLong("-1");
        Optional<DataSample> foundOpt = sampleRepository.findById(id);
        assertFalse(foundOpt.isPresent());
    }
}
