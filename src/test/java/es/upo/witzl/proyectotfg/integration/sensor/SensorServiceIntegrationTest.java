package es.upo.witzl.proyectotfg.integration.sensor;

import es.upo.witzl.proyectotfg.samples.dto.SensorDto;
import es.upo.witzl.proyectotfg.samples.error.SensorAlreadyExistsException;
import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.repository.SensorRepository;
import es.upo.witzl.proyectotfg.samples.service.ISensorService;
import es.upo.witzl.proyectotfg.samples.service.SensorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class SensorServiceIntegrationTest {
    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public ISensorService sensorService() {
            return new SensorService();
        }
    }

    @Autowired
    private ISensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    @Before
    public void setUp() {
        Sensor sensor = new Sensor();
        sensor.setId(Long.parseLong("1"));
        sensor.setAlias("TEST");
        sensor.setName("sensor_name");

        Mockito.when(sensorRepository.findById(sensor.getId())).thenReturn(Optional.of(sensor));
        Mockito.when(sensorRepository.findByAlias(sensor.getAlias())).thenReturn(Optional.of(sensor));
        Mockito.when(sensorRepository.findByName(sensor.getName())).thenReturn(Optional.of(sensor));
    }

    @Test
    public void whenValidId_thenSensorShouldBeFound() {
        String alias = "TEST";

        Optional<Sensor> foundOpt = sensorService.getSensorById(Long.parseLong("1"));
        assertTrue(foundOpt.isPresent());
    }

    @Test
    public void whenInValidId_thenSensorShouldNotBeFound() {
        Optional<Sensor> notFoundOpt = sensorService.getSensorById(Long.parseLong("-1"));
        assertFalse(notFoundOpt.isPresent());
    }

    @Test
    public void whenValidAlias_thenSensorShouldNotBeFound() {
        Optional<Sensor> foundOpt = sensorService.getSensorByAlias("TEST");
        assertTrue(foundOpt.isPresent());
    }

    @Test
    public void whenInValidAlias_thenSensorShouldNotBeFound() {
        Optional<Sensor> notFoundOpt = sensorService.getSensorByAlias("NOT");
        assertFalse(notFoundOpt.isPresent());
    }

    @Test
    public void whenValidName_thenSensorShouldBeFound() {
        Optional<Sensor> foundOpt = sensorService.getSensorByName("sensor_name");
        assertTrue(foundOpt.isPresent());
    }

    @Test
    public void whenInValidName_thenSensorShouldNotBeFound() {
        Optional<Sensor> foundOpt = sensorService.getSensorByName("not_sensor_name");
        assertFalse(foundOpt.isPresent());
    }

    @Test(expected = SensorAlreadyExistsException.class)
    public void whenSaveExistingSensor_throwsSensorAlreadyExistsException() {
        SensorDto sensorDto = new SensorDto();
        sensorDto.setAlias("TEST");
        sensorDto.setName("sensor_name");
        sensorDto.setDescription("other_sensor_description");
        sensorService.registerNewSensor(sensorDto);
    }
}
