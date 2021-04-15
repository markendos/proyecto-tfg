package es.upo.witzl.proyectotfg.integration.sensor;

import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.repository.SensorRepository;
import es.upo.witzl.proyectotfg.spring.PersistenceJPAConfig;
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
import java.util.Optional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(
        classes = { PersistenceJPAConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
@ActiveProfiles("test")
public class SensorRepositoryIntegrationTest {

    @Resource
    private SensorRepository sensorRepository;

    private Sensor testSensor;

    @Before
    public void setUp() {
        Sensor sensor = new Sensor();
        sensor.setAlias("TEST");
        sensor.setName("test_sensor_name");
        sensor.setDescription("test_sensor_description");

        testSensor = sensorRepository.save(sensor);
    }

    @Test
    public void whenValidId_thenSampleShouldBeFound() {
        Long id = testSensor.getId();
        Optional<Sensor> foundOpt = sensorRepository.findById(id);
        assertTrue(foundOpt.isPresent());
        Sensor found = foundOpt.get();
        assertEquals(found.getId(), id);
        assertEquals(found.getAlias(), "TEST");
        assertEquals(found.getName(), "test_sensor_name");
        assertEquals(found.getDescription(), "test_sensor_description");
    }

    @Test
    public void whenInValidId_thenSensorShouldNotBeFound() {
        Long id = Long.parseLong("-1");
        Optional<Sensor> foundOpt = sensorRepository.findById(id);
        assertFalse(foundOpt.isPresent());
    }
}
