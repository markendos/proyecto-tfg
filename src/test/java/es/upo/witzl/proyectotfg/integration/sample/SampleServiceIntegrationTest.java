package es.upo.witzl.proyectotfg.integration.sample;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.samples.model.DataChannel;
import es.upo.witzl.proyectotfg.samples.model.DataSample;
import es.upo.witzl.proyectotfg.samples.model.DataValue;
import es.upo.witzl.proyectotfg.samples.repository.*;
import es.upo.witzl.proyectotfg.samples.service.ISampleService;
import es.upo.witzl.proyectotfg.samples.service.SampleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SampleServiceIntegrationTest {
    @TestConfiguration
    static class ProjectServiceTestContextConfiguration {
        @Bean
        public ISampleService sampleService() {
            return new SampleService();
        }
    }

    @Autowired
    private ISampleService sampleService;

    @MockBean
    private DataSampleRepository sampleRepository;

    @MockBean
    private DataChannelRepository dataChannelRepository;

    @MockBean
    private DataValueRepository dataValueRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private SensorRepository sensorRepository;

    @MockBean
    private ChannelStatisticsRepository statisticsRepository;

    private DataSample testSample;

    @Before
    public void setUp() {
        Project project = new Project();
        project.setId(Long.parseLong("1"));

        DataSample sample1 = new DataSample();
        sample1.setId(Long.parseLong("1"));
        sample1.setProject(project);

        DataChannel dChannel = new DataChannel();
        dChannel.setChannelName("A1");
        dChannel.setDataSample(sample1);

        DataValue dValue = new DataValue();
        dValue.setId(sample1.getId() + "@" + dChannel.getChannelName());
        List<Integer> values = new ArrayList<>();

        for(int i = 0; i < 100; i++) {
            values.add(i);
        }
        dValue.setValues(values);
        sample1.setDataChannels(Arrays.asList(dChannel));
        testSample = sample1;

        Mockito.when(sampleRepository.findById(sample1.getId())).thenReturn(Optional.of(sample1));
        Mockito.when(dataValueRepository.findById(dValue.getId())).thenReturn(Optional.of(dValue));
    }

    @Test
    public void whenValidId_thenProjectShouldBeFound() {
        Long id = Long.parseLong("1");

        Optional<DataSample> foundOpt = sampleService.getSampleById(id);
        assertTrue(foundOpt.isPresent());

        DataSample found = foundOpt.get();
        assertEquals(found.getId(), id);
    }

    @Test
    public void whenInValidId_thenProjectShouldNotBeFound() {
        Long id = Long.parseLong("-1");

        Optional<DataSample> foundOpt = sampleService.getSampleById(id);
        assertFalse(foundOpt.isPresent());
    }

    @Test
    public void whenGetSampleValues_thenSampleValueListShouldBeReturned() {
        List<List<Integer>> sampleValues = sampleService.getSampleValues(testSample);
        System.out.println(testSample);

        assertThat(sampleValues).hasSize(1);
        assertThat(sampleValues.get(0)).hasSize(100);
    }
}
